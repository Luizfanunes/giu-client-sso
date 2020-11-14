package br.coop.unimed.giuclientsso.service;


import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;
import br.coop.unimed.giuclientsso.exception.SSOUnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationService {

    public final static String _LOGIN_PATH = "/login";

    public final static String _X_GIU_COOKIE_NAME = "X-CSRF-TOKEN";
    public final static String _X_INTERNAL_COOKIE_NAME = "X-AUTH-TOKEN";
    public final static String _X_INTERNAL_COOKIE_SAME_ORIGIN = "Strict";

    public final static String _AUTHORIZATION_HEADER = "Authorization";
    public final static String _AUTHORIZATION_HEADER_TYPE = "Bearer ";

    @Autowired
    private SSOService ssoService;

    public LoginResponseDTO login(String authCode, String codigoUnimed) throws SSOUnauthorizedException {
        log.debug("Recebendo requisição de autenticação.");
        if (authCode == null || authCode.isEmpty()) {
            log.error("Não foi informado o authorization code.");
            throw new SSOUnauthorizedException();
        }
        return ssoService.login(authCode, codigoUnimed);
    }
}