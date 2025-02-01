package dev.bti.starters.exceptions.auth;

import dev.bti.starters.common.Constants;
import org.springframework.http.HttpStatus;

public class ResetCodeIncorrectException extends AuthException {
    public ResetCodeIncorrectException() {
        super(HttpStatus.BAD_REQUEST, Constants.Auth._400);
    }
}
