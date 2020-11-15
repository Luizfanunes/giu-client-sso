package br.coop.unimed.giuclientsso.model.jwt;

import br.coop.unimed.giuclientsso.model.sessao.AplicacaoPapelSSO;
import br.coop.unimed.giuclientsso.model.sessao.SessaoSSO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestGeneratorTokenApplication {
    @NotNull
    private TemplateToken ssoToken;
    @NotNull
    private String[] papeis;
    @NotNull
    private String codigoUnimed;

    public RequestGeneratorTokenApplication(TemplateToken accessToken, String[] papeis, String codigoUnimed) {
        this.ssoToken = accessToken;
        this.papeis = papeis;
        this.codigoUnimed = codigoUnimed;
    }

    public RequestGeneratorTokenApplication(TemplateToken accessToken, SessaoSSO sessao, String codigoUnimed) {
        this(accessToken, sessao.getPapeis().stream().map(AplicacaoPapelSSO::getNome).toArray(String[]::new), codigoUnimed);
    }
}
