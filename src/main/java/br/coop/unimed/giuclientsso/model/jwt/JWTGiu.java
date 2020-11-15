package br.coop.unimed.giuclientsso.model.jwt;

import br.coop.unimed.giuclientsso.config.Constantes;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JWTGiu {
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
}
