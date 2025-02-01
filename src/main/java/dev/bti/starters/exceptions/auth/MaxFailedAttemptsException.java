package dev.bti.starters.exceptions.auth;

import dev.bti.starters.common.Constants;
import org.springframework.http.HttpStatus;

public class MaxFailedAttemptsException extends AuthException {
    public MaxFailedAttemptsException() {
        super(HttpStatus.UNAUTHORIZED, Constants.Auth._403);
    }
}
