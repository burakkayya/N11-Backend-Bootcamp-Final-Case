package com.burakkaya.commentservice.api.clients;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "restaurant-service", fallback = RestaurantClientFallback.class)
public interface RestaurantClient {
    @GetMapping(value = "api/restaurants/check-restaurant-exists/{restaurantId}")
    ClientResponse checkIfRestaurantExists(@PathVariable String restaurantId);
}
