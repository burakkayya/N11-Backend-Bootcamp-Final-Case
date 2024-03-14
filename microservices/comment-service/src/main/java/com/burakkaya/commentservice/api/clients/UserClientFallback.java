package com.burakkaya.commentservice.api.clients;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class UserClientFallback implements UserClient{
    @Override
    public ClientResponse checkIfUserExists(UUID userId) {
        log.info("USER SERVICE IS DOWN");
        throw new RuntimeException("USER SERVICE IS DOWN");
    }
}
