package br.coop.unimed.giuclientsso.model;

import br.coop.unimed.giuclientsso.model.sessao.SessaoSSO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String applicationToken;
    private SessaoSSO dadosSessao;
}
