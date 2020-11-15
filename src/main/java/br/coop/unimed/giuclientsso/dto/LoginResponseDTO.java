package br.coop.unimed.giuclientsso.dto;

import br.coop.unimed.giuclientsso.model.SessaoSSO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private SessaoSSO sessao;
    private String cookie;
    private String applicationAccessToken;
}
