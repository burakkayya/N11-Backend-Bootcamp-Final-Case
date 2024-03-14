package com.burakkaya.userservice.api.controllers;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.userservice.business.abstracts.UserService;
import com.burakkaya.userservice.business.dto.requests.CreateUserRequest;
import com.burakkaya.userservice.business.dto.requests.UpdateUserRequest;
import com.burakkaya.userservice.business.dto.responses.CreateUserResponse;
import com.burakkaya.userservice.business.dto.responses.GetAllUsersResponse;
import com.burakkaya.userservice.business.dto.responses.GetUserResponse;
import com.burakkaya.userservice.business.dto.responses.UpdateUserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final UserService service;

    @GetMapping
    public List<GetAllUsersResponse> getAll(){
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public GetUserResponse getById(@PathVariable UUID id){
        return service.getUserById(id);
    }

    @PostMapping
    public CreateUserResponse add(@Valid @RequestBody CreateUserRequest request){
        return service.createUser(request);
    }

    @PutMapping("/{id}")
    public UpdateUserResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request){
        return service.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        service.deleteUser(id);
    }

    @GetMapping("/check-user-exists/{id}")
    public ClientResponse checkIfUserExists(@PathVariable UUID id){
        return service.checkIfUserExists(id);
    }

}
