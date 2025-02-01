package dev.bti.starters.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception extends java.lang.Exception {

    HttpStatus status;

    public Exception(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
