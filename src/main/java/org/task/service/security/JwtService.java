package org.task.service.security;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtService {

    private final String USERNAME_PARAMETER = "username";
    private final String ROLES_PARAMETER = "roles";

    private final int EXPIRATION_TIME_MILLIS = 300_000; //5 min

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtService(Algorithm algorithm, JWTVerifier verifier) {
        this.algorithm = algorithm;
        this.verifier = verifier;
    }

    public String createJwtToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return JWT.create()
            .withClaim(USERNAME_PARAMETER, username)
            .withClaim(ROLES_PARAMETER, convertAuthorities(authorities))
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
            .sign(algorithm);
    }

    public User parseToken(String token) {
        DecodedJWT jwtToken = verifier.verify(token);
        return new User(jwtToken.getClaim(USERNAME_PARAMETER).asString(),
            "",
            convertRoles(jwtToken.getClaim(ROLES_PARAMETER).asList(String.class)));
    }

    private List<String> convertAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    private List<GrantedAuthority> convertRoles(List<String> roles) {
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
