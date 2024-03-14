package com.burakkaya.restaurantservice.business.rules;

import com.burakkaya.commonpackage.utils.constants.Messages;
import com.burakkaya.commonpackage.utils.exceptions.BusinessException;
import com.burakkaya.restaurantservice.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RestaurantBusinessRules {
    private final RestaurantRepository repository;

    public void checkIfRestaurantExists(String id){
        if(!repository.existsById(id)) throw new BusinessException(Messages.Restaurant.NotExists);
    }
}
