package org.omnera.picbloom.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

//This class is a service that generates JWT tokens.
@Service
public class TokenService {

    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder){
        this.encoder=encoder;
    }


    public String generateToken(Authentication authentication){
        Instant now = Instant.now();//Get the current time for marking when the token was created.

    //Extract the user’s roles/authorities from Spring Security’s Authentication object.
        String scope =authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));

    //building the payload of the JWT token (the “claims”)
        JwtClaimsSet claims = JwtClaimsSet.builder()
                                .issuer("self")//who created the token
                                .issuedAt(now)//when the token was created.
                                .expiresAt(now.plus(1,ChronoUnit.HOURS))//token expiration time
                                .subject(authentication.getName())//he user’s identity (e.g., username/email).
                                .claim("scope", scope)//custom claim containing the user’s roles.
                                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
}
