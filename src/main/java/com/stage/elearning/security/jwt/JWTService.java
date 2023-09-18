package com.stage.elearning.security.jwt;

import com.stage.elearning.model.user.UserEntity;
import com.stage.elearning.security.utility.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Component
public class JWTService {

    public String generateToken(@NotNull UserEntity userEntity)
    {
        String email = userEntity.getEmail();
        Date currentData = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + SecurityConstants.ACCESS_JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentData)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256,getSignInKey())
                .compact();
    }
    public boolean validateToken(String token) {
        try{
            Claims claims = extractAllClaims(token);
        }
        catch (ExpiredJwtException ex)
        {
            throw new ExpiredJwtException(null,null,"Token has expired. Please log in again.", ex);
        }

        return true;
    }

    public boolean isTokenExpired(String token)
    {
        return extractExpirationDate(token).before(new Date());
    }
    public boolean isTokenValid(String token, @NotNull UserEntity userEntity)
    {
        final String email = extractEmailFromJwt(token);
        return email.equals(userEntity.getEmail()) && !isTokenExpired(token);
    }
    public <T> T extractClaim(String token , @NotNull Function<Claims,T> claimResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    public Claims extractAllClaims(String token)
    {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new ExpiredJwtException(null,null,"Token has expired. Please log in again.");
        }
    }

    public Date extractExpirationDate(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    public Date extractIssuedAtDate(String token)
    {
        return extractClaim(token , Claims::getIssuedAt);
    }
    public String extractEmailFromJwt(String token)
    {
        return extractClaim(token , Claims::getSubject);
    }


    private Key getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_ACCESS_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
