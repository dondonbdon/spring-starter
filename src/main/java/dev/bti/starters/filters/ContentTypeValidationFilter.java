package dev.bti.starters.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bti.starters.models.res.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ContentTypeValidationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> SUPPORTED_JSON_CONTENT_TYPES = List.of("application/json");
    private final List<String> SUPPORTED_FORM_CONTENT_TYPES = List.of("multipart/form-data");
    private final List<String> SUPPORTED_XML_CONTENT_TYPES = List.of("application/xml");

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/api/v1/media/upload")) {
            Boolean valid = isContentTypeValid(request, SUPPORTED_FORM_CONTENT_TYPES);

            if (valid == null) {
                sendContentTypeEmptyResponse(response);
                return;
            }
            if (valid) {
                sendUnsupportedMediaTypeResponse(request, response, "multipart/form-data");
                return;
            }
        } else if (requestURI.startsWith("/api/v1/xml")) {
            Boolean valid = isContentTypeValid(request, SUPPORTED_XML_CONTENT_TYPES);

            if (valid == null) {
                sendContentTypeEmptyResponse(response);
                return;
            }
            if (valid) {
                sendUnsupportedMediaTypeResponse(request, response, "application/xml");
                return;
            }

        } else if (requestURI.startsWith("/api/v1/media/get")) {
            chain.doFilter(request, response);
            return;
        } else {
            Boolean valid = isContentTypeValid(request, SUPPORTED_JSON_CONTENT_TYPES);

            if (valid == null) {
                sendContentTypeEmptyResponse(response);
                return;
            }
            if (valid) {
                sendUnsupportedMediaTypeResponse(request, response, "application/json");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private Boolean isContentTypeValid(HttpServletRequest request, List<String> validContentTypes) {

        String contentType = request.getContentType();

        if (contentType == null) {
            return null;
        }

        if (contentType.startsWith("multipart/form-data")) {
            return false;
        }

        return !validContentTypes.contains(contentType);
    }


    private void sendUnsupportedMediaTypeResponse(HttpServletRequest request, HttpServletResponse response, String expectedContentType)
            throws IOException {
        response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        response.setContentType("application/json");

        response.getWriter().write(objectMapper.writeValueAsString(Response.builder()
                .message(String.format("Unsupported Content-Type. Expected %s Received %s", expectedContentType, request.getContentType()))
                .success(false)
                .build()));
    }

    private void sendContentTypeEmptyResponse(HttpServletResponse response)
            throws IOException {
        response.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        response.setContentType("application/json");

        response.getWriter().write(objectMapper.writeValueAsString(Response.builder()
                .message("Content-Type is empty. Expected a valid Content-Type")
                .success(false)
                .build()));
    }
}
