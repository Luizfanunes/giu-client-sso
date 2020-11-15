package br.coop.unimed.giuclientsso.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioToken {
    private String nome;
    private String login;
    private Long userId;
    private String codigoUnimed;
    private String[] papeis;
    private boolean isAuthenticated;
    private String accessToken;
    private boolean contaServico;

}
