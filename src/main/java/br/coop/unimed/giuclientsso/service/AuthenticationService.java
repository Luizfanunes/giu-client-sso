package br.coop.unimed.giuclientsso.service;


import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;
import br.coop.unimed.giuclientsso.exception.SSOUnauthorizedException;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.JWTAuthenticationApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class AuthenticationService {

    @Autowired
    private SSOService ssoService;

    public LoginResponseDTO login(String authCode, String unimedCode) throws SSOUnauthorizedException {
        log.debug("Recebendo requisição de autenticação.");
        if (StringUtils.isEmpty(authCode)) {
            log.error("Não foi informado o authorization code.");
            throw new SSOUnauthorizedException();
        }
        return ssoService.login(authCode, unimedCode);
    }

    public UsuarioToken atualizarSessao(JWTAuthenticationApplication jwtAuthApp, String cookie) {
        log.debug("Recebendo requisição de atualizar sessão.");
        return ssoService.atualizarSessao(jwtAuthApp, cookie);
    }
}