package com.burakkaya.userservice.business.concretes;

import com.burakkaya.commonpackage.utils.enums.Status;
import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import com.burakkaya.userservice.business.abstracts.UserService;
import com.burakkaya.userservice.business.dto.requests.CreateUserRequest;
import com.burakkaya.userservice.business.dto.requests.UpdateUserRequest;
import com.burakkaya.userservice.business.dto.responses.CreateUserResponse;
import com.burakkaya.userservice.business.dto.responses.GetAllUsersResponse;
import com.burakkaya.userservice.business.dto.responses.GetUserResponse;
import com.burakkaya.userservice.business.dto.responses.UpdateUserResponse;
import com.burakkaya.userservice.entities.User;
import com.burakkaya.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserManager implements UserService {
    private final UserRepository userRepository;
    private final ModelMapperService mapper;
    @Override
    public List<GetAllUsersResponse> getAllUsers() {
        List<GetAllUsersResponse> responses = userRepository.findAll()
                .stream()
                .map(user -> mapper.forResponse().map(user, GetAllUsersResponse.class))
                .toList();
        return responses;
    }

    @Override
    public GetUserResponse getUserById(UUID id) {
        User  user = userRepository.findById(id).orElseThrow();
        GetUserResponse response = mapper.forResponse().map(user, GetUserResponse.class);
        return response;
    }

    @Override
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        User user = mapper.forRequest().map(createUserRequest, User.class);
        user.setId(null);
        user.setStatus(Status.ACTIVE);
        User createdUser = userRepository.save(user);
        CreateUserResponse response = mapper.forResponse().map(createdUser, CreateUserResponse.class);
        return response;
    }

    @Override
    public UpdateUserResponse updateUser(UUID id, UpdateUserRequest updateUserRequest) {
        User user = mapper.forRequest().map(updateUserRequest, User.class);
        user.setId(id);
        User updatedUser = userRepository.save(user);
        UpdateUserResponse response = mapper.forResponse().map(updatedUser, UpdateUserResponse.class);
        return response;
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setStatus(Status.PASSIVE);
        userRepository.save(user);
    }
}
