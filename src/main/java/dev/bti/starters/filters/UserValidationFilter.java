package dev.bti.starters.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bti.starters.models.res.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


@Component
public class UserValidationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {


        String path = request.getRequestURI();

        if (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/media") || path.startsWith("/api/v1/security")) {
            chain.doFilter(request, response);
            return;
        }

        String userIdFromPath = extractUserIdFromPath(path);
        String jwtUserId = extractUserIdFromJWT();

        if (userIdFromPath == null) {
            chain.doFilter(request, response);
            return;
        }

        Logger.getLogger("Filter").info(userIdFromPath);
        if (!userIdFromPath.equals(jwtUserId)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");

            response.getWriter().write(objectMapper.writeValueAsString(Response.builder()
                    .message("Access Denied: User ID mismatch.")
                    .success(false)
                    .build()));
            return;
        }

        chain.doFilter(request, response);
    }

    private String extractUserIdFromPath(String path) {
        List<String> segments = List.of(path.split("/"));

        if (segments.contains("user")) {
            return segments.get(segments.indexOf("user") + 1);
        }

        return null;
    }

    private String extractUserIdFromJWT() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return null;
    }
}