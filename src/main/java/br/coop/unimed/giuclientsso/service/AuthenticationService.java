package br.coop.unimed.giuclientsso.service;


import br.coop.unimed.giuclientsso.exception.SSOUnauthorizedException;
import br.coop.unimed.giuclientsso.model.AuthenticationModel;
import br.coop.unimed.giuclientsso.model.LoginResponse;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.ApplicationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class AuthenticationService {

    @Autowired
    private SSOService ssoService;

    public LoginResponse login(String authCode, String unimedCode) throws SSOUnauthorizedException {
        log.debug("Recebendo requisição de autenticação.");
        if (StringUtils.isEmpty(authCode)) {
            log.error("Não foi informado o authorization code.");
            throw new SSOUnauthorizedException();
        }
        AuthenticationModel authenticationModel = ssoService.login(authCode, unimedCode);

        return new LoginResponse(JwtService.generate(authenticationModel.getApplicationAccessToken()), authenticationModel.getDadosSessao());
    }

    public UsuarioToken atualizarSessao(ApplicationToken jwtAuthApp, String cookie) {
        log.debug("Recebendo requisição de atualizar sessão.");
        return ssoService.atualizarSessao(jwtAuthApp, cookie);
    }
}