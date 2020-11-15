package br.coop.unimed.giuclientsso.rest.facade;

import br.coop.unimed.giuclientsso.config.Constantes;
import br.coop.unimed.giuclientsso.config.SsoProperties;
import br.coop.unimed.giuclientsso.enumerator.Erro;
import br.coop.unimed.giuclientsso.exception.GiuException;
import br.coop.unimed.giuclientsso.exception.base.BaseSSORuntimeException;
import br.coop.unimed.giuclientsso.exception.model.GIUClassError;
import br.coop.unimed.giuclientsso.model.AuthenticationModel;
import br.coop.unimed.giuclientsso.model.jwt.RequestGeneratorTokenApplication;
import br.coop.unimed.giuclientsso.model.jwt.TemplateToken;
import br.coop.unimed.giuclientsso.model.sessao.AplicacaoSSO;
import br.coop.unimed.giuclientsso.model.sessao.SessaoSSO;
import br.coop.unimed.giuclientsso.model.sessao.UsuarioUnimedSSO;
import br.coop.unimed.giuclientsso.model.token.TokenCookieOutput;
import br.coop.unimed.giuclientsso.model.token.TokenOutput;
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
    private static final String _REFRESH_TOKEN = "/api/token";
    private static final String _OBTEM_SESSAO = "/api/usuario/obtem-sessao";

    @Autowired
    private SsoProperties ssoProperties;

    public AuthenticationModel authenticationByAuthCode(String authCode, String unimedCode) {
        if (StringUtils.isEmpty(unimedCode))
            throw new BaseSSORuntimeException(Erro.NAO_INFORMADO_UNIMED);

        TokenCookieOutput tokenCookieOutputDTO = getTokenByCode(authCode);
        SessaoSSO sessao = getSessao(tokenCookieOutputDTO.getAccessToken(), tokenCookieOutputDTO.getCookie(), unimedCode);

        return new AuthenticationModel(sessao, new RequestGeneratorTokenApplication(new TemplateTokenGiu(tokenCookieOutputDTO.getAccessToken()), sessao, unimedCode), tokenCookieOutputDTO.getCookie());
    }

    public TokenCookieOutput refreshToken(String authCode, String authCookie) {
        if (StringUtils.isEmpty(authCode) || StringUtils.isEmpty(authCookie))
            throw new BaseSSORuntimeException(Erro.AUTH_TOKEN_COOKIE_NAO_INFORMADO);

        ResponseEntity<TokenOutput> response = getNewRestTemplate().exchange(
                ssoProperties.getUrlGiu() + _REFRESH_TOKEN,
                HttpMethod.POST,
                new HttpEntity<>(new RefreshTokenRequest(), includeAuthenticationParameters(authCode, authCookie, null)),
                TokenOutput.class
        );
        return new TokenCookieOutput(Objects.requireNonNull(response.getBody()), mapCookie(response));
    }

    private TokenCookieOutput getTokenByCode(String authCode) {
        if (StringUtils.isEmpty(authCode))
            throw new BaseSSORuntimeException(Erro.AUTH_TOKEN_NAO_INFORMADO);

        ResponseEntity<TokenOutput> response = getNewRestTemplate().exchange(
                ssoProperties.getUrlGiu() + _OBTEM_TOKEN,
                HttpMethod.POST,
                new HttpEntity<>(new AuthByCodeRequest(authCode, ssoProperties.getRedirectUriAplicacao(), ssoProperties.getClientIdAplicacao())),
                TokenOutput.class
        );
        return new TokenCookieOutput(Objects.requireNonNull(response.getBody()), mapCookie(response));
    }

    private SessaoSSO getSessao(String authToken, String authCookie, String unimedCode) {
        SessaoSSO sessao = getNewRestTemplate().exchange(
                ssoProperties.getUrlGiu() + _OBTEM_SESSAO,
                HttpMethod.GET,
                new HttpEntity<>(includeAuthenticationParameters(authToken, authCookie, unimedCode)),
                SessaoSSO.class
        ).getBody();
        includeRoles(Objects.requireNonNull(sessao), unimedCode);
        return sessao;
    }

    private String mapCookie(ResponseEntity<TokenOutput> response) {
        String cookie = Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0);
        return cookie.replace(Constantes._X_COOKIE_NAME + "=", "").split(";")[0];
    }

    private HttpHeaders includeAuthenticationParameters(String authToken, String authCookie, String unimedCode) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (!StringUtils.isEmpty(authToken)) {
            httpHeaders.add(Constantes._AUTHORIZATION_HEADER, Constantes._AUTHORIZATION_HEADER_TYPE + authToken);
        }
        if (!StringUtils.isEmpty(authCookie)) {
            httpHeaders.add(HttpHeaders.COOKIE, Constantes._X_COOKIE_NAME + "=" + authCookie);
        }
        if (!StringUtils.isEmpty(unimedCode)) {
            httpHeaders.add(Constantes._X_UNIMED_HEADER, unimedCode);
        }
        return httpHeaders;
    }

    private void includeRoles(SessaoSSO sessao, @NotNull String codigoUnimed) {
        Optional<UsuarioUnimedSSO> optUnimed = sessao.getUsuarioUnimed().stream().filter(usuarioUnimedSSO -> usuarioUnimedSSO.getUnimed().getCodigo().equals(Long.valueOf(codigoUnimed))).findFirst();
        if (optUnimed.isPresent()) {
            Optional<AplicacaoSSO> optAplicacao = optUnimed.get().getAplicacoes().stream().filter(aplicacao -> aplicacao.getClientId().equals(ssoProperties.getClientIdAplicacao())).findFirst();
            if (optAplicacao.isPresent()) {
                sessao.setPapeis(optAplicacao.get().getPapeis());
            } else {
                throw new BaseSSORuntimeException(Erro.USUARIO_NAO_POSSUI_APLICACAO);
            }
        } else {
            throw new BaseSSORuntimeException(Erro.USUARIO_NAO_POSSUI_UNIMED);
        }
    }

    private RestTemplate getNewRestTemplate() {
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
        throw new GiuException(giuError.getErrorCode(), giuError.getErrorDescription(), response.getStatusCode());
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

@Data
class RefreshTokenRequest {
    private final String grant_type = "refresh_token";
}

class TemplateTokenGiu implements TemplateToken {
    private String accessToken;

    public TemplateTokenGiu(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }
}
