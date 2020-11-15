package br.coop.unimed.giuclientsso.filter;

import br.coop.unimed.giuclientsso.config.Constantes;
import br.coop.unimed.giuclientsso.exception.GiuException;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.ApplicationToken;
import br.coop.unimed.giuclientsso.service.AuthenticationService;
import br.coop.unimed.giuclientsso.service.JwtService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Configuration
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    private UsuarioToken usuarioToken;

    protected boolean shouldNotFilter(HttpServletRequest httpReq) {
        if (httpReq.getMethod().contains(HttpMethod.OPTIONS.name()) || !httpReq.getRequestURI().startsWith(Constantes._API_PREFIX_PATH))
            return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpRes, FilterChain chain){
        try {
            String authCode = httpReq.getHeader(Constantes._AUTHORIZATION_HEADER);
            String authCookie = Objects.isNull(httpReq.getCookies()) ? null : Arrays.stream(httpReq.getCookies()).filter(v -> v.getName().equals(Constantes._X_COOKIE_NAME)).map(Cookie::getValue).findFirst().orElse(null);

            //Caso o usuário não envie o token, a requisição poderá prosseguir. Apenas salvando o status que o usuário não está autenticado. E Caso esteja acessando uma rota segura, esse requisição será tratada posteriormente pelo AopAuthorizer.
            if (StringUtils.isEmpty(authCode) || authCode.trim().isEmpty()) {
                this.usuarioToken = new UsuarioToken(null, null, null, null, null, false, authCode, false);
                chain.doFilter(httpReq, httpRes);
                return;
            }

            authCode = authCode.replace(Constantes._AUTHORIZATION_HEADER_TYPE, "").trim();

            ApplicationToken jwt = JwtService.mapApplicationToken(authCode);

            this.usuarioToken = authenticationService.atualizarSessao(jwt, authCookie);

            if (!StringUtils.isEmpty(usuarioToken.getSsoToken()) && !usuarioToken.getSsoToken().equals(jwt.getSsoToken())) {
                httpRes.addHeader(Constantes._AUTHORIZATION_HEADER_NEW, Constantes._AUTHORIZATION_HEADER_TYPE + usuarioToken.getSsoToken());
            }
            chain.doFilter(httpReq, httpRes);
        } catch (JWTVerificationException e) {
            //TODO jwt app invalido;
            logger.error("Deu ruim " + e.getMessage());
            httpRes.setStatus(HttpStatus.FORBIDDEN.value());
        } catch (GiuException e) {
            logger.error("Deu ruim " + e.getMessage());
            httpRes.setStatus(e.getHttpCode().value());
        } catch (Exception e) {
            logger.error("Deu ruim " + e.getMessage());
        }

    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UsuarioToken usuarioToken() {
        return Optional.ofNullable(this.usuarioToken).orElseThrow(() -> new IllegalArgumentException());
    }
}
