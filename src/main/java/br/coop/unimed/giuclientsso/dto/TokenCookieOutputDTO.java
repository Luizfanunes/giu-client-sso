package br.coop.unimed.giuclientsso.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TokenCookieOutputDTO extends TokenOutputDTO {

    @JsonIgnore
    private String cookie;

    public TokenCookieOutputDTO(String accessToken, Long expiresIn, boolean senhaExpirada, String tokenId, String userId, String cookie) {
        super(accessToken, expiresIn, senhaExpirada, tokenId, userId);
        this.cookie = cookie;
    }

    public TokenCookieOutputDTO(TokenOutputDTO tokenOutputDTO, String cookie) {
        super(tokenOutputDTO.getAccessToken(), tokenOutputDTO.getExpiresIn(), tokenOutputDTO.isSenhaExpirada(), tokenOutputDTO.getTokenId(), tokenOutputDTO.getUserId());
        this.cookie = cookie;
    }

    public String getCookie() {
        return this.cookie;
    }
}
