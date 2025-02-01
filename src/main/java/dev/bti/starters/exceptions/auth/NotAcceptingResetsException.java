package dev.bti.starters.exceptions.auth;

import dev.bti.starters.common.Constants;
import org.springframework.http.HttpStatus;

public class NotAcceptingResetsException extends AuthException {
    public NotAcceptingResetsException() {
        super(HttpStatus.BAD_REQUEST, Constants.Auth._400);
    }
}
