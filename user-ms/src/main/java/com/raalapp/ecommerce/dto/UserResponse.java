package com.raalapp.ecommerce.dto;

import com.raalapp.ecommerce.models.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String keyCloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private AddressDTO address;
}
