package br.coop.unimed.giuclientsso.model.jwt;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ApplicationToken {
    private String ssoToken;
    private String[] papeis;
    private String codigoUnimed;
}