package br.coop.unimed.giuclientsso.exception;

import br.coop.unimed.giuclientsso.enumerator.Erro;

public class BaseSSOException extends Exception {
    private Erro erro;

    public BaseSSOException(Erro erro) {
        this.erro = erro;
    }

    public Erro getErro() {
        return this.erro;
    }
}
