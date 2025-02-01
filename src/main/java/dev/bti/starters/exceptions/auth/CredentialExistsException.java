package dev.bti.starters.exceptions.auth;

import org.springframework.http.HttpStatus;

public class CredentialExistsException extends AuthException {
    public CredentialExistsException(String msg) {
        super(HttpStatus.FORBIDDEN, msg);
    }
}
