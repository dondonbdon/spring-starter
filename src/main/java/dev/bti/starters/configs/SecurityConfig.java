package dev.bti.starters.configs;

import dev.bti.starters.filters.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RateLimitingFilter rateLimitingFilter;
    private final RouteExistenceFilter routeExistenceFilter;
    private final JwtVerificationFilter jwtVerificationFilter;
    private final ContentTypeValidationFilter contentTypeValidationFilter;
    private final UserValidationFilter userValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auths -> auths
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/terminal/**",
                                "/api/terminal/**",
                                "/api/v1/security/**")
                        .permitAll()
                        .requestMatchers("/api/v1/user/**",
                                "/api/v1/cart/**",
                                "/api/v1/item/**",
                                "/api/v1/items/**",
                                "/api/v1/order/**",
                                "/api/v1/stock/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(routeExistenceFilter, RateLimitingFilter.class);
        http.addFilterBefore(jwtVerificationFilter, RouteExistenceFilter.class);
        http.addFilterBefore(contentTypeValidationFilter, JwtVerificationFilter.class);
        http.addFilterAfter(userValidationFilter, JwtVerificationFilter.class);

        return http.build();
    }
}


