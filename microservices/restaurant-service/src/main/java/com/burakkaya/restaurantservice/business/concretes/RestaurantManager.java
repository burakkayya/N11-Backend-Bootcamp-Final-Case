package com.burakkaya.restaurantservice.business.concretes;

import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.enums.Status;
import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import com.burakkaya.restaurantservice.business.abstracts.RestaurantService;
import com.burakkaya.restaurantservice.business.dto.requests.CreateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.requests.UpdateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.responses.CreateRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetAllRestaurantsResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.UpdateRestaurantResponse;
import com.burakkaya.restaurantservice.entities.Restaurant;
import com.burakkaya.restaurantservice.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantManager implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapperService modelMapperService;

    @Override
    public List<GetAllRestaurantsResponse> getAllRestaurants() {
        List<GetAllRestaurantsResponse> responses = new ArrayList<>();
        restaurantRepository.findAll().forEach(restaurant -> {
            GetAllRestaurantsResponse response = modelMapperService.forResponse().map(restaurant, GetAllRestaurantsResponse.class);
            responses.add(response);
        });
        return responses;
    }

    @Override
    public GetRestaurantResponse getRestaurant(String id) {
        GetRestaurantResponse response = modelMapperService.forResponse().map(restaurantRepository.findById(id).orElseThrow(), GetRestaurantResponse.class);
        return response;
    }

    @Override
    public CreateRestaurantResponse createRestaurant(CreateRestaurantRequest createRestaurantRequest) {
        Restaurant restaurant = modelMapperService.forRequest().map(createRestaurantRequest, Restaurant.class);
        restaurant.setStatus(Status.ACTIVE);
        restaurant.setState(State.OPEN);
        Restaurant createdRestaurant = restaurantRepository.save(restaurant);
        CreateRestaurantResponse response = modelMapperService.forResponse().map(createdRestaurant, CreateRestaurantResponse.class);
        return response;
    }

    @Override
    public UpdateRestaurantResponse updateRestaurant(String id, UpdateRestaurantRequest updateRestaurantRequest) {
        Restaurant restaurant = modelMapperService.forRequest().map(updateRestaurantRequest, Restaurant.class);
        restaurant.setId(id);
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        UpdateRestaurantResponse response = modelMapperService.forResponse().map(updatedRestaurant, UpdateRestaurantResponse.class);
        return response;
    }

    @Override
    public void deleteRestaurant(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        restaurant.setStatus(Status.PASSIVE);
        restaurantRepository.save(restaurant);
    }
}