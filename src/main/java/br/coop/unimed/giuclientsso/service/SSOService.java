package br.coop.unimed.giuclientsso.service;

import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;
import br.coop.unimed.giuclientsso.dto.TokenCookieOutputDTO;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.JWTAuthenticationApplication;

public interface SSOService {

    LoginResponseDTO login(String authCode, String unimedCode);

    UsuarioToken atualizarSessao(JWTAuthenticationApplication jwtAuthApp, String cookie);
}
