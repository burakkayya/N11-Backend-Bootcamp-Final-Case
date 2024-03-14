package com.burakkaya.recommendationservice.business.rules;

import com.burakkaya.commonpackage.utils.exceptions.BusinessException;
import com.burakkaya.recommendationservice.api.clients.RestaurantClient;
import com.burakkaya.recommendationservice.api.clients.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@AllArgsConstructor
@Service
public class RecommendationBusinessRules {
    private final RestaurantClient restaurantClient;
    private final UserClient userClient;

    public void checkIfRestaurantExists(String restaurantId){
        var response = restaurantClient.checkIfRestaurantExists(restaurantId);
        if(!response.isSuccess()){
            throw new BusinessException(response.getMessage());
        }
    }

    public void checkIfUserExists(UUID userId){
        var response = userClient.checkIfUserExists(userId);
        if(!response.isSuccess()){
            throw new BusinessException(response.getMessage());
        }
    }
}
