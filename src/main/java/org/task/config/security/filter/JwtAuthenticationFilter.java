package org.task.config.security.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.task.cache.UserCache;
import org.task.service.security.JwtService;

public class JwtAuthenticationFilter implements Filter {

    private final String ACCESS_TOKEN_PARAM_NAME = "access_token";
    private final UserCache userCache;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(UserCache userCache, JwtService jwtService) {
        this.userCache = userCache;
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = request.getParameter(ACCESS_TOKEN_PARAM_NAME);
        if (accessToken != null) {
            try {
                jwtService.parseToken(accessToken);
            } catch (TokenExpiredException e) {
                userCache.eraseToken(accessToken);
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                //token is invalid
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Optional<Authentication> authentication = userCache.getAuthentication(accessToken);
            if (authentication.isPresent()) {
                jwtService.parseToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication.get());
            }
        }
        chain.doFilter(request, response);
    }

}
