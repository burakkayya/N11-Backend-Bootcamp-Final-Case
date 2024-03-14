package com.burakkaya.restaurantservice.api.controllers;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.restaurantservice.business.abstracts.RestaurantService;
import com.burakkaya.restaurantservice.business.dto.requests.CreateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.requests.UpdateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.responses.CreateRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetAllRestaurantsResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.UpdateRestaurantResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantsController {
    RestaurantService service;

    @GetMapping
    public List<GetAllRestaurantsResponse> getAll(){
        return service.getAllRestaurants();
    }

    @GetMapping("/{id}")
    public GetRestaurantResponse getById(@PathVariable String id){
        return service.getRestaurant(id);
    }

    @PostMapping
    public CreateRestaurantResponse add(@Valid @RequestBody CreateRestaurantRequest request){
        return service.createRestaurant(request);
    }

    @PutMapping("/{id}")
    public UpdateRestaurantResponse update(@PathVariable String id, @Valid @RequestBody UpdateRestaurantRequest request){
        return service.updateRestaurant(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        service.deleteRestaurant(id);
    }

    @GetMapping("/check-restaurant-exists/{id}")
    public ClientResponse checkIfRestaurantExists(@PathVariable String id){
        return service.checkIfRestaurantExists(id);
    }
}
