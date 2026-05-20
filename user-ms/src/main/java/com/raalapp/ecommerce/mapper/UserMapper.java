package com.raalapp.ecommerce.mapper;

import com.raalapp.ecommerce.dto.UserRequest;
import com.raalapp.ecommerce.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromUserRequestToUser(UserRequest userRequest);
}
