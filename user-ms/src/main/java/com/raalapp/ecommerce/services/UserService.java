package com.raalapp.ecommerce.services;

import com.raalapp.ecommerce.dto.AddressDTO;
import com.raalapp.ecommerce.dto.UserRequest;
import com.raalapp.ecommerce.dto.UserResponse;
import com.raalapp.ecommerce.mapper.UserMapper;
import com.raalapp.ecommerce.models.Address;
import com.raalapp.ecommerce.models.User;
import com.raalapp.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
//    private List<User> userList = new ArrayList<>();
//    private Long nextId = 1L;

    public List<UserResponse> fetchAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest){
//        user.setId(nextId++);
        /*String token = keyCloakAdminService.getAdminAccessToken();
        String keycloakUserId =
                keyCloakAdminService.createUser(token, userRequest);

        User user = new User();
        updateUserFromRequest(user, userRequest);
        user.setKeycloakId(keycloakUserId);

        keyCloakAdminService.assignRealmRoleToUser(userRequest.getUsername(),
                "USER", keycloakUserId);*/
        User user = userMapper.fromUserRequestToUser(userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> fetchUser(String id) {
        return parseUserId(id)
                .flatMap(userRepository::findById)
                .map(this::mapToUserResponse);
    }

    public boolean updateUser(String id, UserRequest updatedUserRequest) {
        return parseUserId(id)
                .flatMap(userRepository::findById)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setKeyCloakId(user.getKeycloakId());
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }
        return response;
    }

    private Optional<String> parseUserId(String id) {
        try {
            return Optional.of(id);
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
