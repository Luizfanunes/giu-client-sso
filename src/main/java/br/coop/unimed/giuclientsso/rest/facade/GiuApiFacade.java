package br.coop.unimed.giuclientsso.rest.facade;

import br.coop.unimed.giuclientsso.config.SsoProperties;
import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;
import br.coop.unimed.giuclientsso.dto.TokenCookieOutputDTO;
import br.coop.unimed.giuclientsso.dto.TokenOutputDTO;
import br.coop.unimed.giuclientsso.enumerator.Erro;
import br.coop.unimed.giuclientsso.exception.BaseSSORuntimeException;
import br.coop.unimed.giuclientsso.exception.GiuException;
import br.coop.unimed.giuclientsso.exception.model.GIUClassError;
import br.coop.unimed.giuclientsso.model.AplicacaoSSO;
import br.coop.unimed.giuclientsso.model.SessaoSSO;
import br.coop.unimed.giuclientsso.model.UsuarioUnimedSSO;
import br.coop.unimed.giuclientsso.model.jwt.JWTAuthenticationApplication;
import br.coop.unimed.giuclientsso.service.AuthenticationService;
import br.coop.unimed.giuclientsso.service.JwtService;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiuApiFacade {

    private static final String _OBTEM_TOKEN = "/api/token";
    private static final String _OBTEM_SESSAO = "/api/usuario/obtem-sessao";

    @Autowired
    private SsoProperties ssoProperties;

    public LoginResponseDTO autenticacaoByAuthCode(String authCode, String codigoUnimed) {
        if (StringUtils.isEmpty(codigoUnimed)) {
            throw new BaseSSORuntimeException(Erro.NAO_INFORMADO_UNIMED);
        }
        TokenCookieOutputDTO tokenCookieOutputDTO = obtemTokenByCode(authCode);
        SessaoSSO sessao = obtemSessao(tokenCookieOutputDTO.getAccessToken(), tokenCookieOutputDTO.getCookie(), codigoUnimed);

        return new LoginResponseDTO(sessao, tokenCookieOutputDTO, JwtService.gerarToken(new JWTAuthenticationApplication(tokenCookieOutputDTO.getAccessToken(), sessao, codigoUnimed)));
    }

    private TokenCookieOutputDTO obtemTokenByCode(String authCode) {
        if (StringUtils.isEmpty(authCode))
            throw new BaseSSORuntimeException(Erro.AUTH_TOKEN_NAO_INFORMADO);

        ResponseEntity<TokenOutputDTO> response = obtemNovoRestTemplate().exchange(
                ssoProperties.getUrlGiu() + _OBTEM_TOKEN,
                HttpMethod.POST,
                new HttpEntity<>(new AuthByCodeRequest(authCode, ssoProperties.getRedirectUriAplicacao(), ssoProperties.getClientIdAplicacao())),
                TokenOutputDTO.class
        );
        return new TokenCookieOutputDTO(Objects.requireNonNull(response.getBody()), obtemCookie(response));
    }

    private SessaoSSO obtemSessao(String authToken, String authCookie, String codigoUnimed) {
        SessaoSSO sessao = obtemNovoRestTemplate().exchange(
                ssoProperties.getUrlGiu() + _OBTEM_SESSAO,
                HttpMethod.GET,
                new HttpEntity<>(incluirAutenticacao(authToken, authCookie, codigoUnimed)),
                SessaoSSO.class
        ).getBody();
        popularPapeis(Objects.requireNonNull(sessao), codigoUnimed);
        return sessao;
    }

    private String obtemCookie(ResponseEntity<TokenOutputDTO> response) {
        String cookie = Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0);
        return cookie.replace(AuthenticationService._X_GIU_COOKIE_NAME + "=", "").split(";")[0];
    }

    private HttpHeaders incluirAutenticacao(String authToken, String authCookie, String codigoUnimed) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (!StringUtils.isEmpty(authToken)) {
            httpHeaders.add(AuthenticationService._AUTHORIZATION_HEADER, AuthenticationService._AUTHORIZATION_HEADER_TYPE + authToken);
        }
        if (!StringUtils.isEmpty(authCookie)) {
            httpHeaders.add("Cookie", AuthenticationService._X_GIU_COOKIE_NAME + "=" + authCookie);
        }
        if (!StringUtils.isEmpty(codigoUnimed)) {
            httpHeaders.add("X-UNIMED-APP", codigoUnimed);
        }
        return httpHeaders;
    }

    private void popularPapeis(SessaoSSO sessao, @NotNull String codigoUnimed) {
        Optional<UsuarioUnimedSSO> optUnimedSelecionada = sessao.getUsuarioUnimed().stream().filter(usuarioUnimedSSO -> usuarioUnimedSSO.getUnimed().getCodigo().equals(Long.valueOf(codigoUnimed))).findFirst();
        if (optUnimedSelecionada.isPresent()) {
            Optional<AplicacaoSSO> optAplicacao = optUnimedSelecionada.get().getAplicacoes().stream().filter(aplicacao -> aplicacao.getClientId().equals(ssoProperties.getClientIdAplicacao())).findFirst();
            if (optAplicacao.isPresent()) {
                sessao.setPapeis(optAplicacao.get().getPapeis());
            } else {
                throw new BaseSSORuntimeException(Erro.USUARIO_NAO_POSSUI_APLICACAO);
            }
        } else {
            throw new BaseSSORuntimeException(Erro.USUARIO_NAO_POSSUI_UNIMED);
        }
    }

    private RestTemplate obtemNovoRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new GiuRestTemplateHandler());
        return restTemplate;
    }
}

@Slf4j
class GiuRestTemplateHandler implements ResponseErrorHandler {
    private static final Gson gson = new Gson();

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String json = new BufferedReader(
                new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        GIUClassError giuError = gson.fromJson(json, GIUClassError.class);
        log.error("Erro ao chamar serviço do GIU. Details: " + giuError.toString());
        throw new GiuException(giuError.getErrorCode(), giuError.getErrorDescription());
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError())
            return true;
        return false;
    }
}

@Data
class AuthByCodeRequest {
    private final String grant_type = "authorization_code";
    private String code;
    private String redirect_uri;
    private String client_id;

    public AuthByCodeRequest(String code, String redirectUri, String clientId) {
        this.code = code;
        this.redirect_uri = redirectUri;
        this.client_id = clientId;
    }
}
