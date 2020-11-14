package br.coop.unimed.giuclientsso.exception.model;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@ToString
public class GIUClassError {
    private String details;
    private GiuCodeError erro;
    private String httpStatus;
    private String timestamp;

    public String getErrorCode() {
        return this.erro.getCodigo();
    }

    public String getErrorDescription() {
        return this.erro.getDescricao();
    }

}

@Data
@ToString
class GiuCodeError {
    private String codigo;
    private String descricao;
}
