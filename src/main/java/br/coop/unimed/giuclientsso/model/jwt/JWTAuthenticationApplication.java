package br.coop.unimed.giuclientsso.model.jwt;

import br.coop.unimed.giuclientsso.model.AplicacaoPapelSSO;
import br.coop.unimed.giuclientsso.model.SessaoSSO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class JWTAuthenticationApplication {
    @NotNull
    private String ssoToken;
    @NotNull
    private String[] papeis;
    @NotNull
    private String codigoUnimed;

    public JWTAuthenticationApplication(String accessToken, String[] papeis, String codigoUnimed) {
        this.ssoToken = accessToken;
        this.papeis = papeis;
        this.codigoUnimed = codigoUnimed;
    }

    public JWTAuthenticationApplication(String accessToken, SessaoSSO sessao, String codigoUnimed) {
        this(accessToken, sessao.getPapeis().stream().map(AplicacaoPapelSSO::getNome).toArray(String[]::new), codigoUnimed);
    }
}
