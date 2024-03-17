package com.burakkaya.recommendationservice.api.controllers;

import com.burakkaya.commonpackage.utils.dto.GetAllRestaurantResponse;
import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.kafka.producer.KafkaProducer;
import com.burakkaya.recommendationservice.business.abstracts.RecommendationService;
import com.burakkaya.recommendationservice.business.dto.requests.RecommendationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecommendationsController.class)
class RecommendationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService mockService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void testGetRecommendations() throws Exception {
        final RecommendationRequest recommendationRequest = new RecommendationRequest(UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409"));

        final GetAllRestaurantResponse getAllRestaurantResponse = new GetAllRestaurantResponse();
        getAllRestaurantResponse.setId("id");
        getAllRestaurantResponse.setName("name");
        getAllRestaurantResponse.setPhoneNumber("phoneNumber");
        getAllRestaurantResponse.setAddress("address");
        getAllRestaurantResponse.setState(State.OPEN);
        final List<GetAllRestaurantResponse> responses = List.of(getAllRestaurantResponse);
        when(mockService.getRecommendations(any(RecommendationRequest.class))).thenReturn(responses);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/recommendations")
                        .content(new ObjectMapper().writeValueAsString(recommendationRequest)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(
                "\"id\":\"id\",\"name\":\"name\",\"phoneNumber\":\"phoneNumber\",\"address\":\"address\",\"state\":\"OPEN\"");
    }

    @Test
    void testGetRecommendations_RecommendationServiceReturnsNoItems() throws Exception {
        final RecommendationRequest recommendationRequest = new RecommendationRequest(UUID.fromString("426ac385-cbad-47b0-8814-f0da1c256409"));

        when(mockService.getRecommendations(any(RecommendationRequest.class))).thenReturn(Collections.emptyList());

        final MockHttpServletResponse response = mockMvc.perform(get("/api/recommendations")
                        .content(new ObjectMapper().writeValueAsString(recommendationRequest)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
