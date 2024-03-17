package com.burakkaya.recommendationservice.business.concretes;

import com.burakkaya.commonpackage.utils.dto.GetAllRestaurantResponse;
import com.burakkaya.commonpackage.utils.dto.GetUserResponse;
import com.burakkaya.recommendationservice.api.clients.RestaurantClient;
import com.burakkaya.recommendationservice.api.clients.UserClient;
import com.burakkaya.recommendationservice.business.dto.requests.RecommendationRequest;
import com.burakkaya.recommendationservice.business.rules.RecommendationBusinessRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationManagerTest {

    @Mock
    private RestaurantClient mockRestaurantClient;
    @Mock
    private UserClient mockUserClient;
    @Mock
    private RecommendationBusinessRules mockRules;

    private RecommendationManager recommendationManagerUnderTest;

    @BeforeEach
    void setUp() {
        recommendationManagerUnderTest = new RecommendationManager(mockRestaurantClient, mockUserClient, mockRules);
    }

    @Test
    void testGetRecommendations() {
        final RecommendationRequest recommendationRequest = new RecommendationRequest(
                UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409"));

        final GetAllRestaurantResponse getAllRestaurantResponse = new GetAllRestaurantResponse();
        getAllRestaurantResponse.setId("id");
        getAllRestaurantResponse.setName("name");
        getAllRestaurantResponse.setLatitude(0.0);
        getAllRestaurantResponse.setLongitude(0.0);
        getAllRestaurantResponse.setRating(0.0);
        final List<GetAllRestaurantResponse> responses = List.of(getAllRestaurantResponse);
        when(mockRestaurantClient.getAllRestaurants()).thenReturn(responses);

        final GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setId(UUID.fromString("f92c3eba-ebb3-43a1-8c0d-a5a641075a6e"));
        getUserResponse.setFirstName("firstName");
        getUserResponse.setLastName("lastName");
        getUserResponse.setLatitude(0.0);
        getUserResponse.setLongitude(0.0);
        when(mockUserClient.getUserById(UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409")))
                .thenReturn(getUserResponse);

        final List<GetAllRestaurantResponse> result = recommendationManagerUnderTest.getRecommendations(
                recommendationRequest);

        verify(mockRules).checkIfUserExists(UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409"));
    }

    @Test
    void testGetRecommendations_RestaurantClientReturnsNoItems() {
        final RecommendationRequest recommendationRequest = new RecommendationRequest(
                UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409"));
        when(mockRestaurantClient.getAllRestaurants()).thenReturn(Collections.emptyList());

        final GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setId(UUID.fromString("f92c3eba-ebb3-43a1-8c0d-a5a641075a6e"));
        getUserResponse.setFirstName("firstName");
        getUserResponse.setLastName("lastName");
        getUserResponse.setLatitude(0.0);
        getUserResponse.setLongitude(0.0);
        when(mockUserClient.getUserById(UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409")))
                .thenReturn(getUserResponse);

        final List<GetAllRestaurantResponse> result = recommendationManagerUnderTest.getRecommendations(
                recommendationRequest);

        assertThat(result).isEqualTo(Collections.emptyList());
        verify(mockRules).checkIfUserExists(UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409"));
    }
}
