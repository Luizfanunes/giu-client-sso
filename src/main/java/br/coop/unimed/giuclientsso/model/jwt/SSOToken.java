package br.coop.unimed.giuclientsso.model.jwt;

import br.coop.unimed.giuclientsso.config.Constantes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class SSOToken implements TemplateToken {

    private String accessToken;

    public SSOToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isExpirate(boolean withTolerance) {
        return isExpirate(withTolerance ? Constantes._REFRESH_TOLERANCE_IN_MINUTES : 0L);
    }

    public boolean isExpirate() {
        return isExpirate(0L);
    }

    private LocalDateTime getExpirationDate() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(getExpirationTime()), TimeZone.getDefault().toZoneId());
    }

    private boolean isExpirate(Long toleranceInMinutes) {
        return getExpirationDate().plusMinutes(toleranceInMinutes).isBefore(LocalDateTime.now());
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }
}
