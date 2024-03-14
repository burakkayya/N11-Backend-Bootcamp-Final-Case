package com.burakkaya.recommendationservice.business.concretes;

import com.burakkaya.commonpackage.utils.dto.GetAllRestaurantResponse;
import com.burakkaya.commonpackage.utils.dto.GetUserResponse;
import com.burakkaya.recommendationservice.api.clients.RestaurantClient;
import com.burakkaya.recommendationservice.api.clients.UserClient;
import com.burakkaya.recommendationservice.business.abstracts.RecommendationService;
import com.burakkaya.recommendationservice.business.dto.requests.RecommendationRequest;
import com.burakkaya.recommendationservice.business.rules.RecommendationBusinessRules;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Service
public class RecommendationManager implements RecommendationService {
    private final RestaurantClient restaurantClient;
    private final UserClient userClient;
    private final RecommendationBusinessRules rules;
    @Override
    public List<GetAllRestaurantResponse> getRecommendations(RecommendationRequest recommendationRequest) {
        rules.checkIfRestaurantExists(recommendationRequest.getRestaurantId());
        rules.checkIfUserExists(recommendationRequest.getUserId());

        List<GetAllRestaurantResponse> restaurants = restaurantClient.getAllRestaurants();
        GetUserResponse user = userClient.getUserById(recommendationRequest.getUserId());

        List<GetAllRestaurantResponse> recommendedRestaurants = new ArrayList<>();
        for(GetAllRestaurantResponse restaurant : restaurants){
            double distance = calculateDistance(user.getLatitude(), user.getLongitude(), restaurant.getLatitude(), restaurant.getLongitude());
            if(distance <= 10.0){
                recommendedRestaurants.add(restaurant);
            }
        }
        recommendedRestaurants.sort((r1, r2) -> Double.compare(weightedScore(r1.getRating(), calculateDistance(user.getLatitude(), user.getLongitude(), r1.getLatitude(), r1.getLongitude())),
                weightedScore(r2.getRating(), calculateDistance(user.getLatitude(), user.getLongitude(), r2.getLatitude(), r2.getLongitude()))));

        return recommendedRestaurants.subList(0, Math.min(recommendedRestaurants.size(), 3));

    }

    //Real world distance calculation with Havarsine formula
    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        return dist * 1.609344;
    }

    private double weightedScore(double rating, double distance) {
        return (0.7 * rating) + (0.3 / distance);
    }
}
