package com.curso.springsecurity.service;

import com.curso.springsecurity.data.entity.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-minutes}")
    private long EXPIRATION_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    /**
     *
     * @param user
     * @param claims
     *
     * setSubject - setIssued and setExpiration are mandatory claims
     *
     * @return
     */
    public String generateToken(User user, Map<String, Object> claims){

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(issuedAt.getTime() + (EXPIRATION_MINUTES * 60 * 1000));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * To generate the secret key:
     * 1 - Create property "SECRET_KEY".
     * 2 - Convert the property value to Base64 and store in byte array.
     * 3 - Return key with the HMAC256 algorithm
     *
     * @return Key value
     */
    private Key generateKey() {
        byte[] rawKey  = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(rawKey);
    }


    /**
     * It validates 3 things:
     *
     * 1 - That the JWT has the correct format.
     * 2 - That the current date is less than the expiry date
     * 3 - That the signature received is valid.
     * @param jwt
     * @return
     */
    public String extractUsername(String jwt) {
        return Jwts.parser().setSigningKey(generateKey()).build()
                .parseClaimsJws(jwt).getBody().getSubject();
    }
}
