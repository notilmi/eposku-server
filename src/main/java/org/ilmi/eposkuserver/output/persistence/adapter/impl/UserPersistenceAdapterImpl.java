package org.ilmi.eposkuserver.output.persistence.adapter.impl;

import org.ilmi.eposkuserver.domain.User;
import org.ilmi.eposkuserver.output.persistence.adapter.UserPersistenceAdapter;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserEntity;
import org.ilmi.eposkuserver.output.persistence.mapper.UserMapper;
import org.ilmi.eposkuserver.output.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPersistenceAdapterImpl implements UserPersistenceAdapter {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserPersistenceAdapterImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {

        UserEntity userEntity = userMapper.toEntity(user);

        return userMapper.toDomain(userRepository.save(userEntity));
    }

    @Override
    public void delete(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        userRepository.delete(userEntity);
    }
}
