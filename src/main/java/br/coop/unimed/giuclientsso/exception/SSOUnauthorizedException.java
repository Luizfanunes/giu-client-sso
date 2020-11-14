package br.coop.unimed.giuclientsso.exception;

import br.coop.unimed.giuclientsso.enumerator.Erro;

public class SSOUnauthorizedException extends BaseSSOException {
    public SSOUnauthorizedException() {
        super(Erro.AUTH_TOKEN_NAO_INFORMADO);
    }

    public SSOUnauthorizedException(Erro erro) {
        super(erro);
    }
}
