package com.burakkaya.userservice.business.rules;

import com.burakkaya.commonpackage.utils.constants.Messages;
import com.burakkaya.commonpackage.utils.exceptions.BusinessException;
import com.burakkaya.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserBusinessRules {
    private final UserRepository repository;

    public void checkIfUserExistsById(UUID id){
        if(!repository.existsById(id)){
            throw new BusinessException(Messages.User.NotExists);
        }
    }
}
