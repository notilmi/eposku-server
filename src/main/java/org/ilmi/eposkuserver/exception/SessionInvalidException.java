package org.ilmi.eposkuserver.exception;

import org.springframework.http.HttpStatus;

public class SessionInvalidException extends StatusfulException {
    public SessionInvalidException(String message) {
        super(
                HttpStatus.BAD_REQUEST,
                "SESSION_INVALID",
                message
        );
    }

    public SessionInvalidException() {
        this("Session invalid");
    }
}
