package br.coop.unimed.giuclientsso.model.sessao;

import lombok.Data;

@Data
public class PerfilSSO {
    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
