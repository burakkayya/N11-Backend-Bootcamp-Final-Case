package com.burakkaya.restaurantservice.repository;

import com.burakkaya.restaurantservice.entities.Restaurant;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.UUID;

public interface RestaurantRepository extends SolrCrudRepository<Restaurant, String> {
}
