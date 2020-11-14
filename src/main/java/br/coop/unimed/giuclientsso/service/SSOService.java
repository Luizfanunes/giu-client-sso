package br.coop.unimed.giuclientsso.service;

import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;

public interface SSOService {

    LoginResponseDTO login(String authCode, String codigoUnimed);
}
