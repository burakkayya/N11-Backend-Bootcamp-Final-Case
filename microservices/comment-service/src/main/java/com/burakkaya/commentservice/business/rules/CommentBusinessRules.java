package com.burakkaya.commentservice.business.rules;

import com.burakkaya.commentservice.api.clients.RestaurantClient;
import com.burakkaya.commentservice.api.clients.UserClient;
import com.burakkaya.commentservice.repository.CommentRepository;
import com.burakkaya.commonpackage.utils.constants.Messages;
import com.burakkaya.commonpackage.utils.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CommentBusinessRules {
    private final CommentRepository repository;
    private final RestaurantClient restaurantClient;
    private final UserClient userClient;

    public void checkIfCommentExistsById(String id){
        if(!repository.existsById(id)){
            throw new BusinessException(Messages.Comment.NotExists);
        }
    }

    public void checkIfRestaurantExists(String restaurantId){
        var response = restaurantClient.checkIfRestaurantExists(restaurantId);
        if(!response.isSuccess()){
            throw new BusinessException(response.getMessage());
        }
    }

    public void checkIfUserExists(String userId){
        var response = userClient.checkIfUserExists(UUID.fromString(userId));
        if(!response.isSuccess()){
            throw new BusinessException(response.getMessage());
        }
    }
}
