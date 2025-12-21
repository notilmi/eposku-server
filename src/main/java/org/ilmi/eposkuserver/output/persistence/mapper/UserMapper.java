package org.ilmi.eposkuserver.output.persistence.mapper;

import org.ilmi.eposkuserver.domain.User;
import org.ilmi.eposkuserver.output.persistence.entity.aggregate.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserEntity toEntity(User domain) {
        return modelMapper.map(domain, UserEntity.class);
    }

    public User toDomain(UserEntity entity) {
        return modelMapper.map(entity, User.class);
    }

}
