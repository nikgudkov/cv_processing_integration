package org.task.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.task.cache.UserCache;
import org.task.config.security.filter.JwtAuthenticationFilter;
import org.task.service.security.JwtService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String jwtSecret;
    private final UserCache userCache;

    public WebSecurityConfiguration(@Value("${jwt-secret}") String jwtSecret, UserCache userCache) {
        this.jwtSecret = jwtSecret;
        this.userCache = userCache;
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user").password(bCryptPasswordEncoder().encode("pass"))
            .authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .httpBasic();

        http.addFilterBefore(new JwtAuthenticationFilter(userCache, jwtService()), BasicAuthenticationFilter.class);

        //disabled it for simplicity, so our POST requests won't need CookieCsrfTokenRepository.DEFAULT_CSRF_COOKIE_NAME cookie
        //we don't need it for REST API design
        http.csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtService jwtService() {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return new JwtService(algorithm, verifier);
    }
}
