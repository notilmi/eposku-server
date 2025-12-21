package org.ilmi.eposkuserver.service.impl;

import org.ilmi.eposkuserver.domain.User;
import org.ilmi.eposkuserver.exception.SessionInvalidException;
import org.ilmi.eposkuserver.exception.SessionNotFoundException;
import org.ilmi.eposkuserver.helper.TokenGenerator;
import org.ilmi.eposkuserver.output.persistence.adapter.UserPersistenceAdapter;
import org.ilmi.eposkuserver.output.persistence.repository.SessionRepository;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.SessionEntity;
import org.ilmi.eposkuserver.security.CustomUserDetails;
import org.ilmi.eposkuserver.service.SessionService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserPersistenceAdapter userPersistenceAdapter;

    public SessionServiceImpl(SessionRepository sessionRepository, UserPersistenceAdapter userPersistenceAdapter) {
        this.sessionRepository = sessionRepository;
        this.userPersistenceAdapter = userPersistenceAdapter;
    }

    @Override
    public String createSession(Long userId) {
        SessionEntity session = new SessionEntity();

        session.setUserId(userId);
        session.setToken(TokenGenerator.generateToken());

        return sessionRepository.save(session).getToken();
    }

    @Override
    public boolean validateSession(String token) {
        return sessionRepository.existsByToken(token);
    }

    @Override
    public void invalidateSession(String token) {
        SessionEntity session = sessionRepository.findByToken(token)
                .orElseThrow(SessionNotFoundException::new);

        sessionRepository.delete(session);
    }

    @Override
    public User getUserBySessionToken(String token) {

        SessionEntity session = sessionRepository.findByToken(token)
                .orElseThrow(SessionNotFoundException::new);

        return userPersistenceAdapter.findById(session.getUserId())
                .orElseThrow(SessionInvalidException::new);
    }

    @Override
    public UsernamePasswordAuthenticationToken getAuthenticationToken(
            String token,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return new UsernamePasswordAuthenticationToken(userDetails, "");
    }

    @Override
    public SessionEntity getSessionByToken(String token) {
        return sessionRepository.findByToken(token)
                .orElseThrow(SessionNotFoundException::new);
    }
}
