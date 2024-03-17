package com.burakkaya.restaurantservice.business.concretes;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.enums.Status;
import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import com.burakkaya.restaurantservice.business.dto.requests.CreateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.requests.UpdateRestaurantRequest;
import com.burakkaya.restaurantservice.business.dto.responses.CreateRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetAllRestaurantsResponse;
import com.burakkaya.restaurantservice.business.dto.responses.GetRestaurantResponse;
import com.burakkaya.restaurantservice.business.dto.responses.UpdateRestaurantResponse;
import com.burakkaya.restaurantservice.business.rules.RestaurantBusinessRules;
import com.burakkaya.restaurantservice.entities.Restaurant;
import com.burakkaya.restaurantservice.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantManagerTest {

    @Mock
    private RestaurantRepository mockRestaurantRepository;
    @Mock
    private ModelMapperService mockModelMapperService;
    @Mock
    private RestaurantBusinessRules mockRules;

    private RestaurantManager restaurantManagerUnderTest;

    @BeforeEach
    void setUp() {
        restaurantManagerUnderTest = new RestaurantManager(mockRestaurantRepository, mockModelMapperService, mockRules);
    }

    @Test
    void testGetAllRestaurants() {
        final Restaurant restaurant = new Restaurant();
        restaurant.setId("id");
        restaurant.setState(State.OPEN);
        restaurant.setStatus(Status.ACTIVE);
        restaurant.setRating(0.0);
        restaurant.setCommentCount(0);
        final Iterable<Restaurant> restaurants = List.of(restaurant);
        when(mockRestaurantRepository.findAll()).thenReturn(restaurants);

        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());

        final List<GetAllRestaurantsResponse> result = restaurantManagerUnderTest.getAllRestaurants();

        assertNotNull(result);
        assertEquals(1, result.size());

        GetAllRestaurantsResponse firstRestaurant = result.get(0);
        assertEquals("id", firstRestaurant.getId());
        assertEquals(State.OPEN, firstRestaurant.getState());
        assertEquals(Status.ACTIVE, firstRestaurant.getStatus());
        assertEquals(0.0, firstRestaurant.getRating());
        assertEquals(0, firstRestaurant.getCommentCount());
    }

    @Test
    void testGetAllRestaurants_RestaurantRepositoryReturnsNoItems() {
        when(mockRestaurantRepository.findAll()).thenReturn(Collections.emptyList());

        final List<GetAllRestaurantsResponse> result = restaurantManagerUnderTest.getAllRestaurants();

        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetRestaurant() {
        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());

        final Restaurant restaurant1 = new Restaurant();
        restaurant1.setId("id");
        restaurant1.setState(State.OPEN);
        restaurant1.setStatus(Status.ACTIVE);
        restaurant1.setRating(0.0);
        restaurant1.setCommentCount(0);
        final Optional<Restaurant> restaurant = Optional.of(restaurant1);
        when(mockRestaurantRepository.findById("id")).thenReturn(restaurant);

        final GetRestaurantResponse result = restaurantManagerUnderTest.getRestaurant("id");

        verify(mockRules).checkIfRestaurantExists("id");
    }

    @Test
    void testGetRestaurant_RestaurantRepositoryReturnsAbsent() {
        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());
        when(mockRestaurantRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantManagerUnderTest.getRestaurant("id"))
                .isInstanceOf(NoSuchElementException.class);
        verify(mockRules).checkIfRestaurantExists("id");
    }

    @Test
    void testCreateRestaurant() {
        final CreateRestaurantRequest createRestaurantRequest = new CreateRestaurantRequest("name", "5319418132",
                "address", 0.0, 0.0);

        ModelMapper mockModelMapper = new ModelMapper();
        when(mockModelMapperService.forRequest()).thenReturn(mockModelMapper);
        when(mockModelMapperService.forResponse()).thenReturn(mockModelMapper);

        final Restaurant restaurant = new Restaurant();
        restaurant.setId("d201b78b-4dec-4560-b96b-f728c02b8618");
        restaurant.setName("name");
        restaurant.setPhoneNumber("phoneNumber");
        restaurant.setAddress("address");
        restaurant.setState(State.OPEN);
        restaurant.setStatus(Status.ACTIVE);
        restaurant.setLatitude(0.0);
        restaurant.setLongitude(0.0);
        restaurant.setRating(0.0);
        restaurant.setCommentCount(0);

        when(mockRestaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);

        final CreateRestaurantResponse result = restaurantManagerUnderTest.createRestaurant(createRestaurantRequest);

        assertNotNull(result);
        assertEquals("d201b78b-4dec-4560-b96b-f728c02b8618", result.getId());
        assertEquals("name", result.getName());
        assertEquals("phoneNumber", result.getPhoneNumber());
        assertEquals("address", result.getAddress());
        assertEquals(State.OPEN, result.getState());
        assertEquals(Status.ACTIVE, result.getStatus());
        assertEquals(0.0, result.getLatitude());
        assertEquals(0.0, result.getLongitude());
        assertEquals(0.0, result.getRating());
        assertEquals(0, result.getCommentCount());
    }

    @Test
    void testUpdateRestaurant() {
        final UpdateRestaurantRequest updateRestaurantRequest = new UpdateRestaurantRequest();
        updateRestaurantRequest.setId("id");
        updateRestaurantRequest.setName("name");
        updateRestaurantRequest.setPhoneNumber("phoneNumber");
        updateRestaurantRequest.setAddress("address");
        updateRestaurantRequest.setState(State.OPEN);

        when(mockModelMapperService.forRequest()).thenReturn(new ModelMapper());
        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());

        final Restaurant entity = new Restaurant();
        entity.setId("id");
        entity.setName("name");
        entity.setPhoneNumber("phoneNumber");
        entity.setAddress("address");
        entity.setState(State.OPEN);
        entity.setStatus(Status.ACTIVE);
        entity.setRating(0.0);
        entity.setCommentCount(0);
        final Optional<Restaurant> restaurant = Optional.of(entity);
        when(mockRestaurantRepository.findById("id")).thenReturn(restaurant);

        when(mockRestaurantRepository.save(any(Restaurant.class))).thenReturn(entity);

        final UpdateRestaurantResponse result = restaurantManagerUnderTest.updateRestaurant("id",
                updateRestaurantRequest);

        verify(mockRules).checkIfRestaurantExists("id");
    }

    @Test
    void testDeleteRestaurant() {
        final Restaurant restaurant1 = new Restaurant();
        restaurant1.setId("id");
        restaurant1.setState(State.OPEN);
        restaurant1.setStatus(Status.ACTIVE);
        restaurant1.setRating(0.0);
        restaurant1.setCommentCount(0);
        final Optional<Restaurant> restaurant = Optional.of(restaurant1);
        when(mockRestaurantRepository.findById("id")).thenReturn(restaurant);

        restaurantManagerUnderTest.deleteRestaurant("id");

        verify(mockRules).checkIfRestaurantExists("id");

        final Restaurant entity = new Restaurant();
        entity.setId("id");
        entity.setState(State.OPEN);
        entity.setStatus(Status.PASSIVE);
        entity.setRating(0.0);
        entity.setCommentCount(0);
        verify(mockRestaurantRepository).save(entity);
    }

    @Test
    void testDeleteRestaurant_RestaurantRepositoryFindByIdReturnsAbsent() {
        when(mockRestaurantRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantManagerUnderTest.deleteRestaurant("id"))
                .isInstanceOf(NoSuchElementException.class);
        verify(mockRules).checkIfRestaurantExists("id");
    }

    @Test
    void testCheckIfRestaurantExists() {
        final ClientResponse result = restaurantManagerUnderTest.checkIfRestaurantExists("id");

        verify(mockRules).checkIfRestaurantExists("id");
    }
}
