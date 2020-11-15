package br.coop.unimed.giuclientsso.model.jwt;

import br.coop.unimed.giuclientsso.config.Constantes;
import lombok.AllArgsConstructor;

import javax.el.MethodNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@AllArgsConstructor
public class SSOToken implements TemplateToken {
    private String tokenId;
    private String name;
    private boolean contaServico;
    private LocalDateTime expiration;
    private Long userId;
    private String username;

    private boolean isExpirate(Long toleranceInMinutes) {
        return expiration.plusMinutes(toleranceInMinutes).isBefore(LocalDateTime.now());
    }

    public boolean isExpirate(boolean withTolerance) {
        return isExpirate(withTolerance ? Constantes._REFRESH_TOLERANCE_IN_MINUTES : 0L);
    }

    public boolean isExpirate() {
        return isExpirate(0L);
    }

    @Override
    public String getTokenId() {
        return tokenId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isContaServico() {
        return contaServico;
    }

    @Override
    public long getExpirationTime() {
        return expiration.toEpochSecond(ZoneOffset.UTC);
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String retrieveAccessToken() {
        throw new MethodNotFoundException();
    }
}
