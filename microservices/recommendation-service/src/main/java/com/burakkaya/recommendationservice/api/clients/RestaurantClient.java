package com.burakkaya.recommendationservice.api.clients;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.commonpackage.utils.dto.GetAllRestaurantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "restaurant-service", fallback = RestaurantClientFallback.class)
public interface RestaurantClient {
    @GetMapping(value = "api/restaurants/check-restaurant-exists/{restaurantId}")
    ClientResponse checkIfRestaurantExists(@PathVariable String restaurantId);

    @GetMapping(value = "api/restaurants")
    List<GetAllRestaurantResponse> getAllRestaurants();
}
