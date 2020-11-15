package br.coop.unimed.giuclientsso.service.impl;

import br.coop.unimed.giuclientsso.model.AuthenticationModel;
import br.coop.unimed.giuclientsso.model.token.TokenCookieOutput;
import br.coop.unimed.giuclientsso.enumerator.Erro;
import br.coop.unimed.giuclientsso.exception.SSOSessaoExpiradaException;
import br.coop.unimed.giuclientsso.exception.base.BaseSSORuntimeException;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.ApplicationToken;
import br.coop.unimed.giuclientsso.model.jwt.SSOToken;
import br.coop.unimed.giuclientsso.rest.facade.GiuApiFacade;
import br.coop.unimed.giuclientsso.service.JwtService;
import br.coop.unimed.giuclientsso.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSOGIUServiceImpl implements SSOService {

    @Autowired
    private GiuApiFacade giuApiFacade;

    @Override
    public AuthenticationModel login(String authCode, String unimedCode) {
        return giuApiFacade.authenticationByAuthCode(authCode, unimedCode);
    }

    @Override
    public UsuarioToken atualizarSessao(ApplicationToken jwtAuthApp, String cookie) {
        if (jwtAuthApp == null || cookie == null) {
            throw new BaseSSORuntimeException(Erro.AUTH_TOKEN_COOKIE_NAO_INFORMADO);
        }

        SSOToken jwtToken = JwtService.mapSsoToken(jwtAuthApp.getSsoToken());
        String accessToken = jwtAuthApp.getSsoToken();

        if (jwtToken.isExpirate()) {
            if (jwtToken.isExpirate(true)) {
                TokenCookieOutput newToken = giuApiFacade.refreshToken(jwtAuthApp.getSsoToken(), cookie);
                accessToken = newToken.getAccessToken();
                jwtToken = JwtService.mapSsoToken(accessToken);
            } else {
                throw new SSOSessaoExpiradaException();
            }
        }
        return new UsuarioToken(jwtToken.getName(), jwtToken.getUsername(), jwtToken.getUserId(), jwtAuthApp.getCodigoUnimed(), jwtAuthApp.getPapeis(), true, accessToken, jwtToken.isContaServico());
    }
}
