package dev.bti.starters.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bti.starters.models.res.Response;
import dev.bti.starters.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static dev.bti.starters.common.Constants.Filters.*;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitingFilter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String userId = getUserIdFromRequest(request);
        if (userId == null) {
            Logger.getLogger("Filters").info("RateLimit Passed");
            chain.doFilter(request, response);
            return;
        }

        String userKey = "rate_limit:user:" + userId + ":" + Instant.now().getEpochSecond() / WINDOW_SIZE;
        Long userRequestCount = redisTemplate.opsForValue().increment(userKey, 1);

        if (userRequestCount != null && userRequestCount == 1) {
            redisTemplate.expire(userKey, WINDOW_SIZE, TimeUnit.SECONDS);
        }

        if (userRequestCount != null && userRequestCount > MAX_USER_REQUESTS) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(Response.builder()
                    .message("User rate limit exceeded. Try again later.")
                    .success(false)
                    .build()));
            return;
        }

        String globalKey = "rate_limit:global:" + Instant.now().getEpochSecond() / WINDOW_SIZE;
        Long globalRequestCount = redisTemplate.opsForValue().increment(globalKey, 1);

        if (globalRequestCount != null && globalRequestCount == 1) {
            redisTemplate.expire(globalKey, WINDOW_SIZE, TimeUnit.SECONDS);
        }

        if (globalRequestCount != null && globalRequestCount > MAX_GLOBAL_REQUESTS) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(Response.builder()
                    .message("Global rate limit exceeded. Try again later.")
                    .success(false)
                    .build()));
            return;
        }

        chain.doFilter(request, response);
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            return JwtUtils.extractUsername(jwt);
        }
        return null;
    }
}
