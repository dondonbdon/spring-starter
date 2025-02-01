package dev.bti.starters.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bti.starters.common.RouteMatcher;
import dev.bti.starters.models.res.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RouteExistenceFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping handlerMapping;
    private RouteMatcher routeMatcher;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    private static final Set<String> EXCLUDED_PATHS = Set.of("/terminal/terminal.html", "/terminal/script.js", "/terminal/styles.css");

    @Override
    protected void initFilterBean() {
        Map<RequestMappingInfo, HandlerMethod> requestMappings = handlerMapping.getHandlerMethods();
        Set<String> endpoints = new HashSet<>();

        requestMappings.forEach((requestMappingInfo, handlerMethod) -> {
            endpoints.addAll(requestMappingInfo.getPatternValues());
        });

        this.routeMatcher = new RouteMatcher(endpoints);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String path = urlPathHelper.getPathWithinApplication(request);

        if (EXCLUDED_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        if (routeMatcher.isMatch(path)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");

            response.getWriter().write(objectMapper.writeValueAsString(Response.builder()
                    .message("Route not found.")
                    .success(false)
                    .build()));
        }
    }
}


