package br.coop.unimed.giuclientsso.model.jwt;

import br.coop.unimed.giuclientsso.service.JwtService;

public interface TemplateToken {

    String _SSO_TOKEN_ID = "tokenId";
    String _SSO_NAME = "name";
    String _SSO_CONTA_SERVICO = "contaServico";
    String _SSO_EXPIRATION = "exp";
    String _SSO_USER_ID = "userId";
    String _SSO_USERNAME = "username";

    String getAccessToken();

    default String getTokenId() {
        return (String) JwtService.getClaim(this.getAccessToken(), _SSO_TOKEN_ID, String.class);
    }

    default String getName() {
        return (String) JwtService.getClaim(this.getAccessToken(), _SSO_NAME, String.class);
    }

    default boolean isContaServico() {
        return (Boolean) JwtService.getClaim(this.getAccessToken(), _SSO_CONTA_SERVICO, Boolean.class);
    }

    default long getExpirationTime() {
        return (Long) JwtService.getClaim(this.getAccessToken(), _SSO_EXPIRATION, Long.class);
    }

    default long getUserId() {
        return (Long) JwtService.getClaim(this.getAccessToken(), _SSO_USER_ID, Long.class);
    }

    default String getUsername() {
        return (String) JwtService.getClaim(this.getAccessToken(), _SSO_USERNAME, String.class);
    }
}
