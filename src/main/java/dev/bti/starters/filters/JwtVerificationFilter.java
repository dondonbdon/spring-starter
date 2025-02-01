package dev.bti.starters.filters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.exceptions.auth.InvalidTokenException;
import dev.bti.starters.models.res.Response;
import dev.bti.starters.services.user.UserService;
import dev.bti.starters.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/v1/auth")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);

                String username = JwtUtils.extractUsername(jwt);

                if (!userService.isTokenValid(username, jwt)) {
                    throw new InvalidTokenException();
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    Claims claims = JwtUtils.extractAllClaims(jwt);

                    if (JwtUtils.validToken(jwt, userDetails.getUsername())) {
                        List<String> roles = objectMapper.convertValue(claims.get("roles"), new TypeReference<>() {
                        });
                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        throw new InvalidTokenException();
                    }
                }
            }
        } catch (AuthException e) {
            respondWithError(response, e.getStatus().value(), e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }

    private void respondWithError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        Response errorResponse = Response.builder()
                .message(message)
                .success(false)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}




