package com.burakkaya.restaurantservice.business.abstracts;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.restaurantservice.business.dto.requests.CreateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.requests.UpdateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.responses.CreateRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetAllRestaurantsResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.UpdateRestaurantResponse;

import java.util.List;

public interface RestaurantService {
    List<GetAllRestaurantsResponse> getAllRestaurants();
    GetRestaurantResponse getRestaurant(String id);
    CreateRestaurantResponse createRestaurant(CreateRestaurantRequest createRestaurantRequest);
    UpdateRestaurantResponse updateRestaurant(String id, UpdateRestaurantRequest updateRestaurantRequest);
    void deleteRestaurant(String id);
    ClientResponse checkIfRestaurantExists(String id);
    void updateRestaurantRatingWhenCommentCreated(String id, int rate);
    void updateRestaurantRatingWhenCommentUpdated(String id, int oldRate, int newRate);
    void updateRestaurantRatingWhenCommentDeleted(String id, int rate);
}
