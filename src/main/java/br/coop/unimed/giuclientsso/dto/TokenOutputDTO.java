package br.coop.unimed.giuclientsso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenOutputDTO {

    @JsonProperty(
            value = "access_token",
            required = true
    )
    private String accessToken;

    @JsonProperty(
            value = "expires_in",
            required = true
    )
    private Long expiresIn;


    @JsonProperty(
            value = "senha_expirada",
            required = true
    )
    private boolean senhaExpirada;

    @JsonProperty(
            value = "token_id",
            required = true
    )
    private String tokenId;

    @JsonProperty(
            value = "token_type",
            required = true
    )
    private final String tokenType = "Bearer";


    @JsonProperty(
            value = "user_id",
            required = true
    )
    private String userId;

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public boolean isSenhaExpirada() {
        return senhaExpirada;
    }

    public String getTokenId() {
        return tokenId;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUserId() {
        return userId;
    }

    public TokenOutputDTO(String accessToken, Long expiresIn, boolean senhaExpirada, String tokenId, String userId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.senhaExpirada = senhaExpirada;
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public TokenOutputDTO(){
    }
}
