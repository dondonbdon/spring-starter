package dev.bti.starters.exceptions.auth;

import dev.bti.starters.common.Constants;
import org.springframework.http.HttpStatus;

public class PasswordIncorrectException extends AuthException {
    public PasswordIncorrectException() {
        super(HttpStatus.UNAUTHORIZED, Constants.Auth._401_1);
    }
}
