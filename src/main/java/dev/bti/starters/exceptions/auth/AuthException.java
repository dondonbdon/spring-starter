package dev.bti.starters.exceptions.auth;

import dev.bti.starters.exceptions.Exception;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends Exception {
    public AuthException(HttpStatus status, String message) {
        super(status, message);
    }
}
