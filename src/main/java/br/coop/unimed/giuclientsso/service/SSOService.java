package br.coop.unimed.giuclientsso.service;

import br.coop.unimed.giuclientsso.model.AuthenticationModel;
import br.coop.unimed.giuclientsso.model.UsuarioToken;
import br.coop.unimed.giuclientsso.model.jwt.ApplicationToken;

public interface SSOService {

    AuthenticationModel login(String authCode, String unimedCode);

    UsuarioToken atualizarSessao(ApplicationToken jwtAuthApp, String cookie);
}
