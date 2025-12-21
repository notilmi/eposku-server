package org.ilmi.eposkuserver.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends StatusfulException {
    public UserNotFoundException(String message) {
        super(
                HttpStatus.NOT_FOUND,
                "USER_NOT_FOUND",
                message
        );
    }

    public UserNotFoundException() {
        this("User not found");
    }
}
