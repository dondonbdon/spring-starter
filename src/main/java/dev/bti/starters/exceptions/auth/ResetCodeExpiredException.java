package dev.bti.starters.exceptions.auth;

import dev.bti.starters.common.Constants;
import org.springframework.http.HttpStatus;

public class ResetCodeExpiredException extends AuthException {
    public ResetCodeExpiredException() {
        super(HttpStatus.GONE, Constants.Auth._410);
    }
}
