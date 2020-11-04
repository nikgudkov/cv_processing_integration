package org.task.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.task.cache.UserCache;
import org.task.service.security.JwtService;

/**
 * Controller for handling authentication accessTokens.
 */
@RestController
public class AccessTokenController {

    private final JwtService jwtService;
    private final UserCache userCache;

    public AccessTokenController(JwtService jwtService, UserCache userCache) {
        this.jwtService = jwtService;
        this.userCache = userCache;
    }

    /**
     * Retrieves an access token.
     * @return access token.
     */
    @GetMapping(value = "/accesstoken")
    public String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtService.createJwtToken( authentication.getName(), authentication.getAuthorities());
        userCache.putAccessToken(token, authentication);
        return token;
    }


}
