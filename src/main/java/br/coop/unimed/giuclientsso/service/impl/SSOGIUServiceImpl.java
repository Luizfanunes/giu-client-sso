package br.coop.unimed.giuclientsso.service.impl;

import br.coop.unimed.giuclientsso.dto.LoginResponseDTO;
import br.coop.unimed.giuclientsso.rest.facade.GiuApiFacade;
import br.coop.unimed.giuclientsso.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SSOGIUServiceImpl implements SSOService {

    @Autowired
    private GiuApiFacade giuApiFacade;

    @Override
    public LoginResponseDTO login(String authCode, String codigoUnimed) {
        return giuApiFacade.autenticacaoByAuthCode(authCode, codigoUnimed);
    }
}
