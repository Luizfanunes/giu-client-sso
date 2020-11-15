package br.coop.unimed.giuclientsso.exception;

import org.springframework.http.HttpStatus;

public class GiuException extends RuntimeException {
    private String codigo;
    private String descricao;
    private HttpStatus httpCode;

    public GiuException(String codigo, String descricao, HttpStatus httpCode) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.httpCode = httpCode;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public HttpStatus getHttpCode() {
        return httpCode;
    }
}
