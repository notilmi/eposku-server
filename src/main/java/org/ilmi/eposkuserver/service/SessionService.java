package org.ilmi.eposkuserver.service;

import org.ilmi.eposkuserver.domain.User;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.SessionEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public interface SessionService {
    /**
     * Create a new session for the given user ID and return the session token.
     * @param userId the ID of the user
     * @return the session token
     */
    String createSession(Long userId);

    /**
     * Validate the session token.
     * @param token the session token
     * @return true if the session is valid, false otherwise
     */
    boolean validateSession(String token);

    /**
     * Invalidate the session associated with the given token.
     * @param token the session token
     */
    void invalidateSession(String token);

    /**
     * Retrieve the user associated with the given session token.
     * @param token the session token
     * @return the User object associated with the session token
     */
    User getUserBySessionToken(String token);

    SessionEntity getSessionByToken(String token);

    UsernamePasswordAuthenticationToken getAuthenticationToken(String token, Authentication authentication);
}
