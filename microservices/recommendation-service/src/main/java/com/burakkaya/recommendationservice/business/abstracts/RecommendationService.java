package com.burakkaya.recommendationservice.business.abstracts;

import com.burakkaya.commonpackage.utils.dto.GetAllRestaurantResponse;
import com.burakkaya.recommendationservice.business.dto.requests.RecommendationRequest;

import java.util.List;

public interface RecommendationService {
    List<GetAllRestaurantResponse> getRecommendations(RecommendationRequest recommendationRequest);
}
