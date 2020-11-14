package br.coop.unimed.giuclientsso.dto;

import br.coop.unimed.giuclientsso.model.SessaoSSO;
import br.coop.unimed.giuclientsso.model.jwt.JWTAuthenticationApplication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private SessaoSSO sessao;
    private TokenCookieOutputDTO token;
    private String applicationAccessToken;
}
