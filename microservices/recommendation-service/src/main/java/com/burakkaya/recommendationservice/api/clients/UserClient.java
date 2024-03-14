package com.burakkaya.recommendationservice.api.clients;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.commonpackage.utils.dto.GetUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {
    @GetMapping(value = "api/users/check-user-exists/{userId}")
    ClientResponse checkIfUserExists(@PathVariable UUID userId);

    @GetMapping(value = "api/users/{userId}")
    GetUserResponse getUserById(@PathVariable UUID userId);
}
