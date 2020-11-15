package br.coop.unimed.giuclientsso.controller;

import br.coop.unimed.giuclientsso.config.Constantes;
import br.coop.unimed.giuclientsso.config.SsoProperties;
import br.coop.unimed.giuclientsso.exception.SSOUnauthorizedException;
import br.coop.unimed.giuclientsso.model.LoginResponse;
import br.coop.unimed.giuclientsso.model.sessao.SessaoSSO;
import br.coop.unimed.giuclientsso.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private SsoProperties ssoProperties;

    @GetMapping(Constantes._AUTH_ROUTE + Constantes._LOGIN_PATH)
    public SessaoSSO login(@RequestParam String authCode, @RequestParam(required = false, name = "codigoUnimed") String unimedCode, HttpServletResponse httpServletResponse) throws SSOUnauthorizedException {
        log.info("Recebendo novo acesso de " + authCode);
        LoginResponse login = authenticationService.login(authCode, unimedCode);
        httpServletResponse.addHeader(Constantes._AUTHORIZATION_HEADER, Constantes._AUTHORIZATION_HEADER_TYPE + login.getApplicationToken());
        return login.getDadosSessao();
    }
}
