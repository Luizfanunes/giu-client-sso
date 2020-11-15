package br.coop.unimed.giuclientsso.model.jwt;

public interface TemplateToken {

    String _SSO_TOKEN_ID = "tokenId";
    String _SSO_NAME = "name";
    String _SSO_CONTA_SERVICO = "contaServico";
    String _SSO_EXPIRATION = "exp";
    String _SSO_USER_ID = "userId";
    String _SSO_USERNAME = "username";

    String getTokenId();
    String getName();
    boolean isContaServico();
    long getExpirationTime();
    long getUserId();
    String getUsername();

    String retrieveAccessToken();

}
