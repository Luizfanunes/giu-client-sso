package br.coop.unimed.giuclientsso.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SessaoSSO {
    private String login;
    private Long userId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<UsuarioUnimedSSO> usuarioUnimed;
    private String email;
    private String nomeUsuario;
    private List<AplicacaoPapelSSO> papeis;
}
