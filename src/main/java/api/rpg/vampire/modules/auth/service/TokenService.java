package api.rpg.vampire.modules.auth.service;

import api.rpg.vampire.modules.user.domain.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService
{
    @Value("${api.security.token.secret}")
    private String secret;


    /*
     * GENERATE TOKEN
     *
     */
    public String generateToken(User user)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("vampire-api")
                    .withSubject(user.getUsername())
                    .withClaim("id", user.getId().toString())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        }
        catch (JWTCreationException exception)
        {
            throw new RuntimeException("Unable to generate token", exception);
        }
    }


    /*
     * VALIDATE TOKEN (ISSUER)
     *
     */
    public String validateTokenSubject(String token)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("vampire-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (JWTVerificationException exception)
        {
            throw new RuntimeException("Invalid or expired JWT Token!", exception);
        }
    }


    /*
     * VALIDATE TOKEN (ID)
     *
     */
    public String validateTokenClaimId(String token)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("vampire-api")
                    .build()
                    .verify(token)
                    .getClaim("id").asString();
        }
        catch (JWTVerificationException exception)
        {
            throw new RuntimeException("Invalid or expired JWT Token!");
        }
    }


    /*
     * TOKEN EXPIRATION
     *
     */
    private Instant generateExpirationDate()
    {
        return LocalDateTime.now().plusHours(4).toInstant(ZoneOffset.of("-03:00"));
    }
}
