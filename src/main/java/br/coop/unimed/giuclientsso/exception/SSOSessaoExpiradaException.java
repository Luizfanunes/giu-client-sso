package br.coop.unimed.giuclientsso.exception;

import br.coop.unimed.giuclientsso.enumerator.Erro;
import br.coop.unimed.giuclientsso.exception.base.BaseSSOException;
import br.coop.unimed.giuclientsso.exception.base.BaseSSORuntimeException;

public class SSOSessaoExpiradaException extends BaseSSORuntimeException {
    public SSOSessaoExpiradaException() {
        super(Erro.SESSAO_EXPIRADA);
    }
}
