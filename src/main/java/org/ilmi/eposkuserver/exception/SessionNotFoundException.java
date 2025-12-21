package org.ilmi.eposkuserver.exception;

import org.springframework.http.HttpStatus;

public class SessionNotFoundException extends StatusfulException {
    public SessionNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                "SESSION_NOT_FOUND",
                message
        );
    }

    public  SessionNotFoundException() {
        this("Session not found");
    }
}
