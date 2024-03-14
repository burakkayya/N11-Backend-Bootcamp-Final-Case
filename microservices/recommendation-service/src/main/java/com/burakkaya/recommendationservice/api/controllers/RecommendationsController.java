package com.burakkaya.recommendationservice.api.controllers;

import com.burakkaya.commonpackage.utils.dto.GetAllRestaurantResponse;
import com.burakkaya.recommendationservice.business.abstracts.RecommendationService;
import com.burakkaya.recommendationservice.business.dto.requests.RecommendationRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationsController {
    private final RecommendationService service;

    @GetMapping
    List<GetAllRestaurantResponse> getRecommendations(@Valid @RequestBody RecommendationRequest recommendationRequest){
        return service.getRecommendations(recommendationRequest);
    }
}
