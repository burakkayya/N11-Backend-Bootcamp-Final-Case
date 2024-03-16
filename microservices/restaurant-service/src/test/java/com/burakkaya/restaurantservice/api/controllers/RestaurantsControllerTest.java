package com.burakkaya.restaurantservice.api.controllers;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.enums.Status;
import com.burakkaya.commonpackage.utils.kafka.producer.KafkaProducer;
import com.burakkaya.restaurantservice.business.abstracts.RestaurantService;
import com.burakkaya.restaurantservice.business.dto.requests.CreateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.requests.UpdateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.responses.CreateRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetAllRestaurantsResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.UpdateRestaurantResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RestaurantsController.class)
class RestaurantsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService mockService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void testGetAll() throws Exception {
        final GetAllRestaurantsResponse getAllRestaurantsResponse = new GetAllRestaurantsResponse();
        getAllRestaurantsResponse.setId("id");
        getAllRestaurantsResponse.setName("name");
        getAllRestaurantsResponse.setPhoneNumber("phoneNumber");
        getAllRestaurantsResponse.setAddress("address");
        getAllRestaurantsResponse.setState(State.OPEN);
        final List<GetAllRestaurantsResponse> getAllRestaurantsResponses = List.of(getAllRestaurantsResponse);
        when(mockService.getAllRestaurants()).thenReturn(getAllRestaurantsResponses);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/restaurants")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains(
                "\"id\":\"id\",\"name\":\"name\",\"phoneNumber\":\"phoneNumber\",\"address\":\"address\",\"state\":\"OPEN\"");
    }

    @Test
    void testGetAll_RestaurantServiceReturnsNoItems() throws Exception {
        when(mockService.getAllRestaurants()).thenReturn(Collections.emptyList());

        final MockHttpServletResponse response = mockMvc.perform(get("/api/restaurants")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetById() throws Exception {
        final GetRestaurantResponse getRestaurantResponse = new GetRestaurantResponse();
        getRestaurantResponse.setId("id");
        getRestaurantResponse.setName("name");
        getRestaurantResponse.setPhoneNumber("phoneNumber");
        getRestaurantResponse.setAddress("address");
        getRestaurantResponse.setState(State.OPEN);
        when(mockService.getRestaurant("id")).thenReturn(getRestaurantResponse);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/restaurants/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"id\":\"id\",\"name\":\"name\",\"phoneNumber\":\"phoneNumber\",\"address\":\"address\",\"state\":\"OPEN\",\"status\":null,\"latitude\":null,\"longitude\":null,\"rating\":null,\"commentCount\":null}");
    }

    @Test
    void testAdd() throws Exception {
        CreateRestaurantRequest request = new CreateRestaurantRequest();
        request.setName("Valid Restaurant");
        request.setPhoneNumber("0987654321");
        request.setAddress("A valid address with enough length");
        request.setLatitude(0.0);
        request.setLongitude(0.0);

        final CreateRestaurantResponse createRestaurantResponse = new CreateRestaurantResponse();
        createRestaurantResponse.setId("id");
        createRestaurantResponse.setName("Valid Restaurant");
        createRestaurantResponse.setPhoneNumber("0987654321");
        createRestaurantResponse.setAddress("A valid address with enough length");
        createRestaurantResponse.setState(State.OPEN);
        createRestaurantResponse.setRating(0.0);
        createRestaurantResponse.setCommentCount(0);
        createRestaurantResponse.setLatitude(null);
        createRestaurantResponse.setLongitude(null);

        when(mockService.createRestaurant(any(CreateRestaurantRequest.class))).thenReturn(createRestaurantResponse);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/restaurants")
                        .content("{\"name\":\"Valid Restaurant\",\"phoneNumber\":\"0987654321\",\"address\":\"A valid address with enough length\",\"latitude\":0.0,\"longitude\":0.0}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"id\":\"id\",\"name\":\"Valid Restaurant\",\"phoneNumber\":\"0987654321\",\"address\":\"A valid address with enough length\",\"state\":\"OPEN\",\"status\":null,\"latitude\":null,\"longitude\":null,\"rating\":0.0,\"commentCount\":0}");
    }

    @Test
    void testUpdate() throws Exception {
        final UpdateRestaurantResponse updateRestaurantResponse = new UpdateRestaurantResponse();
        updateRestaurantResponse.setId("id");
        updateRestaurantResponse.setName("Valid Restaurant");
        updateRestaurantResponse.setPhoneNumber("0987654321");
        updateRestaurantResponse.setAddress("A valid address with enough length");
        updateRestaurantResponse.setStatus(Status.ACTIVE);
        updateRestaurantResponse.setState(State.OPEN);
        updateRestaurantResponse.setLatitude(0.0);
        updateRestaurantResponse.setLongitude(0.0);
        when(mockService.updateRestaurant(eq("id"), any(UpdateRestaurantRequest.class)))
                .thenReturn(updateRestaurantResponse);

        UpdateRestaurantRequest request = new UpdateRestaurantRequest();
        request.setId("id");
        request.setName("Valid Restaurant");
        request.setPhoneNumber("0987654321");
        request.setAddress("A valid address with enough length");
        request.setStatus(Status.ACTIVE);
        request.setState(State.OPEN);
        request.setLatitude(0.0);
        request.setLongitude(0.0);

        final MockHttpServletResponse response = mockMvc.perform(put("/api/restaurants/{id}", "id")
                        .content(objectMapper.writeValueAsString(request))  // Use objectMapper to serialize request
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"id\":\"id\",\"name\":\"Valid Restaurant\",\"phoneNumber\":\"0987654321\",\"address\":\"A valid address with enough length\",\"state\":\"OPEN\",\"status\":\"ACTIVE\",\"latitude\":0.0,\"longitude\":0.0,\"rating\":null,\"commentCount\":null}");  // Match the actual response
    }

    @Test
    void testDelete() throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(delete("/api/restaurants/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
        verify(mockService).deleteRestaurant("id");
    }

    @Test
    void testCheckIfRestaurantExists() throws Exception {
        when(mockService.checkIfRestaurantExists("id")).thenReturn(new ClientResponse(false, "message"));

        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/restaurants/check-restaurant-exists/{id}", "id")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"message\":\"message\",\"success\":false}");
    }
}
