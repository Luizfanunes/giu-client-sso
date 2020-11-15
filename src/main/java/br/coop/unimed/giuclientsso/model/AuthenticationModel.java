package br.coop.unimed.giuclientsso.model;

import br.coop.unimed.giuclientsso.model.sessao.SessaoSSO;
import br.coop.unimed.giuclientsso.model.jwt.RequestGeneratorTokenApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationModel {
    private SessaoSSO dadosSessao;
    private RequestGeneratorTokenApplication applicationAccessToken;
    private String cookie;
}
