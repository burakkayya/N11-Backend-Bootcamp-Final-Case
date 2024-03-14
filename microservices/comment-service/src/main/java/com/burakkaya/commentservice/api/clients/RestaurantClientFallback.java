package com.burakkaya.commentservice.api.clients;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestaurantClientFallback implements RestaurantClient{
    @Override
    public ClientResponse checkIfRestaurantExists(String restaurantId) {
        log.info("RESTAURANT SERVICE IS DOWN");
        throw new RuntimeException("RESTAURANT SERVICE IS DOWN");
    }
}
