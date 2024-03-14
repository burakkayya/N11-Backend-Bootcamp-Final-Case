package com.burakkaya.recommendationservice.api.clients;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.commonpackage.utils.dto.GetAllRestaurantResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RestaurantClientFallback implements RestaurantClient{
    @Override
    public ClientResponse checkIfRestaurantExists(String restaurantId) {
        log.info("RESTAURANT SERVICE IS DOWN");
        throw new RuntimeException("RESTAURANT SERVICE IS DOWN");
    }

    @Override
    public List<GetAllRestaurantResponse> getAllRestaurants() {
        log.info("RESTAURANT SERVICE IS DOWN");
        throw new RuntimeException("RESTAURANT SERVICE IS DOWN");
    }
}
