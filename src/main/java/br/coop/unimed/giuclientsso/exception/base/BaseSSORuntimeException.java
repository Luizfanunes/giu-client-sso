package br.coop.unimed.giuclientsso.exception.base;

import br.coop.unimed.giuclientsso.enumerator.Erro;

public class BaseSSORuntimeException extends RuntimeException {
    private Erro erro;

    public BaseSSORuntimeException(Erro erro) {
        this.erro = erro;
    }

    public Erro getErro() {
        return this.erro;
    }
}
