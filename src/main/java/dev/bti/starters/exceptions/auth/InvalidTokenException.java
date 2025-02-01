package dev.bti.starters.exceptions.auth;

import dev.bti.starters.common.Constants;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException() {
        super(HttpStatus.UNAUTHORIZED, Constants.Auth._401);
    }
}
