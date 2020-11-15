package br.coop.unimed.giuclientsso.service;

import br.coop.unimed.giuclientsso.config.Constantes;
import br.coop.unimed.giuclientsso.model.jwt.RequestGeneratorTokenApplication;
import br.coop.unimed.giuclientsso.model.jwt.SSOToken;
import br.coop.unimed.giuclientsso.model.jwt.ApplicationToken;
import br.coop.unimed.giuclientsso.model.jwt.TemplateToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TimeZone;

public class JwtService {

    private static String secret = "tKc+CfHyFp25n_k62F?Z4vEp-jwB*AN-+W&Uj^B5PAr@3dXykv&ftvn6K5EpDdEayXXe+r+cGq&%nmAB9euuBHn4VL_5QU7_@fnC8Yq?6_s7r*t+TTa9SmB4ArR8%bJkR5hk?Gh2+Eefb4@=7J4^=rKLUD-e8C$bXsf!2ZDLt=85^?sD*UEkvcB5S3TP_5dU%%R8H4f@J-3^J38wzPdq846XLRYFWm7nZ^wdKF!9Za9zdJ93jqU*GhBe^5tG99xs";

    private static final String _APPLICATION_ACCESS_TOKEN = "accessToken";
    private static final String _APPLICATION_ROLES = "roles";
    private static final String _APPLICATION_CODIGO_UNIMED = "codigoUnimed";

    private static Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret);
    }

    public static String generate(@Valid RequestGeneratorTokenApplication jwt) {
        return JWT.create()
                .withClaim(_APPLICATION_ACCESS_TOKEN, jwt.getSsoToken().getAccessToken())
                .withClaim(_APPLICATION_ROLES, String.join(",", jwt.getPapeis()))
                .withClaim(_APPLICATION_CODIGO_UNIMED, jwt.getCodigoUnimed())
                .sign(getAlgorithm());
    }

    public static ApplicationToken mapApplicationToken(String applicationToken) {
        if (applicationToken.startsWith(Constantes._AUTHORIZATION_HEADER_TYPE)) {
            applicationToken = applicationToken.substring(Constantes._AUTHORIZATION_HEADER_TYPE.length());
        }

        Map<String, Claim> claims = JWT.require(getAlgorithm())
                .build()
                .verify(applicationToken).getClaims();

        return new ApplicationToken(
                claims.get(_APPLICATION_ACCESS_TOKEN).asString(),
                claims.get(_APPLICATION_ROLES).asString().split(","),
                claims.get(_APPLICATION_CODIGO_UNIMED).asString()
        );
    }

    public static Object getClaim(String token, String claim, Class clazz) {
        return JWT.decode(token).getClaim(claim).as(clazz);
    }

    private JwtService() {
    }
}

