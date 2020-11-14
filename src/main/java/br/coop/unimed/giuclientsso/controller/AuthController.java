package br.coop.unimed.giuclientsso.controller;

import br.coop.unimed.giuclientsso.config.SsoProperties;
import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;
import br.coop.unimed.giuclientsso.exception.SSOUnauthorizedException;
import br.coop.unimed.giuclientsso.model.SessaoSSO;
import br.coop.unimed.giuclientsso.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SsoProperties ssoProperties;

    @GetMapping(AuthenticationService._LOGIN_PATH)
    public SessaoSSO login(@RequestParam String authCode, @RequestParam(required = false) String codigoUnimed, HttpServletResponse httpServletResponse) throws SSOUnauthorizedException {
        LoginResponseDTO response = authenticationService.login(authCode, codigoUnimed);

        ResponseCookie cookie = ResponseCookie.from(AuthenticationService._X_INTERNAL_COOKIE_NAME, response.getToken().getCookie())
                .domain(ssoProperties.getPainelUrlAplicacao())
                .httpOnly(true)
                .secure(true)
                .sameSite(AuthenticationService._X_INTERNAL_COOKIE_SAME_ORIGIN)
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());
        httpServletResponse.addHeader(AuthenticationService._AUTHORIZATION_HEADER, AuthenticationService._AUTHORIZATION_HEADER_TYPE + response.getApplicationAccessToken());

        return response.getSessao();
    }
}
