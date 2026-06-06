package com.opencommerce.authservice.mapper;
import com.opencommerce.authservice.dto.response.UserResponse;
import com.opencommerce.authservice.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUuid(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles().stream().map(role->role.getName().name()).collect(Collectors.toSet())
        );
    }
}
