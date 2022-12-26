package com.example.megalab.security;

import com.example.megalab.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private UserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String authorizationHeader;

    @Value("${jwt.secrete}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMilliseconds;


    public JwtTokenProvider(@Qualifier("userDetailsServiceImp") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login, String role){
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);
        SecretKey secretKey64 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey64)
//                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        SecretKey secretKey64 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secretKey64).build().parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (JwtException | IllegalArgumentException e){
            throw new JwtAuthenticationException("JWT Token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }

    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserName(String token){
        SecretKey secretKey64 = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.parserBuilder().setSigningKey(secretKey64).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){
        return request.getHeader(authorizationHeader);
    }
}
