package com.burakkaya.userservice.business.abstracts;

import com.burakkaya.userservice.business.dto.requests.CreateUserRequest;
import com.burakkaya.userservice.business.dto.requests.UpdateUserRequest;
import com.burakkaya.userservice.business.dto.responses.CreateUserResponse;
import com.burakkaya.userservice.business.dto.responses.GetAllUsersResponse;
import com.burakkaya.userservice.business.dto.responses.GetUserResponse;
import com.burakkaya.userservice.business.dto.responses.UpdateUserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<GetAllUsersResponse> getAllUsers();
    GetUserResponse getUserById(UUID id);
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
    UpdateUserResponse updateUser(UUID id, UpdateUserRequest updateUserRequest);
    void deleteUser(UUID id);

}
