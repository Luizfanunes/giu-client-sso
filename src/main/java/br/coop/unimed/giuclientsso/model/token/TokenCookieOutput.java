package br.coop.unimed.giuclientsso.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TokenCookieOutput extends TokenOutput {

    @JsonIgnore
    private String cookie;

    public TokenCookieOutput(String accessToken, Long expiresIn, boolean senhaExpirada, String tokenId, String userId, String cookie) {
        super(accessToken, expiresIn, senhaExpirada, tokenId, userId);
        this.cookie = cookie;
    }

    public TokenCookieOutput(TokenOutput tokenOutputDTO, String cookie) {
        super(tokenOutputDTO.getAccessToken(), tokenOutputDTO.getExpiresIn(), tokenOutputDTO.isSenhaExpirada(), tokenOutputDTO.getTokenId(), tokenOutputDTO.getUserId());
        this.cookie = cookie;
    }

    public String getCookie() {
        return this.cookie;
    }
}
