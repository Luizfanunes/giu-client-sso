package br.coop.unimed.giuclientsso.model.sessao;


import lombok.Data;

import java.util.List;

@Data
public class UsuarioUnimedSSO {
    private Long id;
    private UnimedSSO unimed;
    private PerfilSSO perfil;
    private List<AplicacaoSSO> aplicacoes;
}
