package br.coop.unimed.giuclientsso.filter;

import br.coop.unimed.giuclientsso.config.Constantes;
import br.coop.unimed.giuclientsso.exception.GiuException;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.JWTAuthenticationApplication;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        if (httpReq.getMethod().contains("OPTIONS") || !httpReq.getRequestURI().startsWith("/api/"))
            return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpReq, HttpServletResponse httpRes, FilterChain chain) throws ServletException, IOException {
        try {
            String authorization = httpReq.getHeader(Constantes._AUTHORIZATION_HEADER);
            String cookie = Objects.isNull(httpReq.getCookies()) ? null : Arrays.stream(httpReq.getCookies()).filter(v -> v.getName().equals(Constantes._X_COOKIE_NAME)).map(Cookie::getValue).findFirst().orElse(null);
            if (StringUtils.isEmpty(authorization)) {
                this.usuarioToken = new UsuarioToken(null, null, null, null, null, false, null, false);
                return;
            }
            if (!authorization.trim().isEmpty()) {
                authorization = authorization.replace(Constantes._AUTHORIZATION_HEADER_TYPE, "").trim();
            }
            JWTAuthenticationApplication jwt = JwtService.recuperarTokenInterno(authorization);
            this.usuarioToken = authenticationService.atualizarSessao(jwt, cookie);

            if (!StringUtils.isEmpty(usuarioToken.getAccessToken()) && !usuarioToken.getAccessToken().equals(authorization)) {
                httpRes.addHeader(Constantes._AUTHORIZATION_HEADER_NEW, Constantes._AUTHORIZATION_HEADER_TYPE + usuarioToken.getAccessToken());
            }
            chain.doFilter(httpReq, httpRes);
        } catch (JWTVerificationException e) {
            //TODO jwt app invalido;
        } catch (GiuException e) {
            // TODO erro ao chamar servico do GIU
        } catch (Exception e) {
            // TODO Default
        }

    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UsuarioToken usuarioToken() {
        return Optional.ofNullable(this.usuarioToken).orElseThrow(() -> new IllegalArgumentException());
    }
}
