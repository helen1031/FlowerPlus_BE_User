package ggamang.flowerplus.security;

import ggamang.flowerplus.users.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {

    public static final String SECRET_KEY = "9c8f2213d72c2e23449a2a5648ece693210f412ffe89c7628a029d5635e4ea0e55b321b11e09f1181b5ad284716bf5e2a3ed92dc899f91bdd6c0d0d6e6d4dec3";

    public String create(UserEntity userEntity) {
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(Long.toString(userEntity.getUserId()))
                .setIssuer("flower plus")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
