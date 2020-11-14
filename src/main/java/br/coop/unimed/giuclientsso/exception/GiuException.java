package br.coop.unimed.giuclientsso.exception;

public class GiuException extends RuntimeException {
    private String codigo;
    private String descricao;

    public GiuException(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
