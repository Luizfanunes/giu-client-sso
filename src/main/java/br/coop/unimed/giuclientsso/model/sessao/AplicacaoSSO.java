package br.coop.unimed.giuclientsso.model.sessao;

import lombok.Data;

import java.util.List;

@Data
public class AplicacaoSSO {
    private Long id;
    private String nome;
    private String clientId;
    private String clientSecret;
    private List<AplicacaoPapelSSO> papeis;
}
