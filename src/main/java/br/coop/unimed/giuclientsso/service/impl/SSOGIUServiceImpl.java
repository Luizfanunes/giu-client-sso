package br.coop.unimed.giuclientsso.service.impl;

import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;
import br.coop.unimed.giuclientsso.dto.TokenCookieOutputDTO;
import br.coop.unimed.giuclientsso.enumerator.Erro;
import br.coop.unimed.giuclientsso.exception.SSOSessaoExpiradaException;
import br.coop.unimed.giuclientsso.exception.base.BaseSSORuntimeException;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.JWTAuthenticationApplication;
import br.coop.unimed.giuclientsso.model.jwt.JWTGiu;
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
    public LoginResponseDTO login(String authCode, String unimedCode) {
        return giuApiFacade.authenticationByAuthCode(authCode, unimedCode);
    }

    @Override
    public UsuarioToken atualizarSessao(JWTAuthenticationApplication jwtAuthApp, String cookie) {
        if (jwtAuthApp == null || cookie == null) {
            throw new BaseSSORuntimeException(Erro.AUTH_TOKEN_COOKIE_NAO_INFORMADO);
        }

        JWTGiu jwtGiu = JwtService.mapGiuToken(jwtAuthApp.getSsoToken());
        String accessToken = jwtAuthApp.getSsoToken();

        if (jwtGiu.isExpirate()) {
            if (jwtGiu.isExpirate(true)) {
                TokenCookieOutputDTO newToken = giuApiFacade.refreshToken(jwtAuthApp.getSsoToken(), cookie);
                accessToken = newToken.getAccessToken();
                jwtGiu = JwtService.mapGiuToken(accessToken);
            } else {
                throw new SSOSessaoExpiradaException();
            }
        }
        return new UsuarioToken(jwtGiu.getName(), jwtGiu.getUsername(), jwtGiu.getUserId(), jwtAuthApp.getCodigoUnimed(), jwtAuthApp.getPapeis(), true, accessToken, jwtGiu.isContaServico());
    }
}
