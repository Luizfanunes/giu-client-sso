package br.coop.unimed.giuclientsso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SsoProperties {

    @Value("${unimed.giu.cookie.domain}")
    private String ssoCookieDomain;

    @Value("${unimed.giu.url}")
    private String urlGiu;

    @Value("${aplicacao.client-id}")
    private String clientIdAplicacao;

    @Value("${aplicacao.painel-url}")
    private String painelUrlAplicacao;

    @Value("${aplicacao.redirect-uri}")
    private String redirectUriAplicacao;

    public String getSsoCookieDomain() {
        return ssoCookieDomain;
    }

    public String getUrlGiu() {
        return urlGiu;
    }

    public String getClientIdAplicacao() {
        return clientIdAplicacao;
    }

    public String getPainelUrlAplicacao() {
        return painelUrlAplicacao;
    }

    public String getRedirectUriAplicacao() {
        return redirectUriAplicacao;
    }


}
