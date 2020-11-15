package br.coop.unimed.giuclientsso.service;

import br.coop.unimed.giuclientsso.config.Constantes;
import br.coop.unimed.giuclientsso.model.jwt.JWTAuthenticationApplication;
import br.coop.unimed.giuclientsso.model.jwt.JWTGiu;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class JwtService {

    private static String secret = "tKc+CfHyFp25n_k62F?Z4vEp-jwB*AN-+W&Uj^B5PAr@3dXykv&ftvn6K5EpDdEayXXe+r+cGq&%nmAB9euuBHn4VL_5QU7_@fnC8Yq?6_s7r*t+TTa9SmB4ArR8%bJkR5hk?Gh2+Eefb4@=7J4^=rKLUD-e8C$bXsf!2ZDLt=85^?sD*UEkvcB5S3TP_5dU%%R8H4f@J-3^J38wzPdq846XLRYFWm7nZ^wdKF!9Za9zdJ93jqU*GhBe^5tG99xs";

    private static final String _ACCESS_TOKEN = "accessToken";
    private static final String _ROLES = "roles";
    private static final String _CODIGO_UNIMED = "codigoUnimed";

    private static final String _GIU_TOKENID = "tokenId";
    private static final String _GIU_NAME = "name";
    private static final String _GIU_CONTA_SERVICO = "contaServico";
    private static final String _GIU_EXPIRATION = "exp";
    private static final String _GIU_USERID = "userId";
    private static final String _GIU_USERNAME = "username";

    private static Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret);
    }

    public static String generateToken(@Valid JWTAuthenticationApplication jwt) {
        return com.auth0.jwt.JWT.create()
                .withClaim(_ACCESS_TOKEN, jwt.getAccessToken())
                .withClaim(_ROLES, Arrays.asList(jwt.getPapeis()).stream().collect(Collectors.joining(",")))
                .withClaim(_CODIGO_UNIMED, jwt.getCodigoUnimed())
                .sign(getAlgorithm());
    }

    public static JWTAuthenticationApplication recuperarTokenInterno(String token) {
        if (token.startsWith(Constantes._AUTHORIZATION_HEADER_TYPE)) {
            token = token.substring(Constantes._AUTHORIZATION_HEADER_TYPE.length());
        }

        Map<String, Claim> claims = JWT.require(getAlgorithm())
                .build()
                .verify(token).getClaims();

        return new JWTAuthenticationApplication(
                claims.get(_ACCESS_TOKEN).asString(),
                claims.get(_ROLES).asString().split(","),
                claims.get(_CODIGO_UNIMED).asString()
        );
    }

    public static JWTGiu recuperarTokenGIU(String giuAccessToken) {
        if (giuAccessToken.startsWith(Constantes._AUTHORIZATION_HEADER_TYPE)) {
            giuAccessToken = giuAccessToken.substring(Constantes._AUTHORIZATION_HEADER_TYPE.length());
        }
        Map<String, Claim> claims = JWT.decode(giuAccessToken).getClaims();
        return new JWTGiu(
                claims.get(_GIU_TOKENID).asString(),
                claims.get(_GIU_NAME).asString(),
                claims.get(_GIU_CONTA_SERVICO).asBoolean(),
                LocalDateTime.ofInstant(Instant.ofEpochSecond(claims.get(_GIU_EXPIRATION).asLong()), TimeZone.getDefault().toZoneId()),
                claims.get(_GIU_USERID).asLong(),
                claims.get(_GIU_USERNAME).asString()
        );
    }

    private JwtService() {
    }
}

