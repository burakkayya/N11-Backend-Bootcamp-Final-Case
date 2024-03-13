package com.burakkaya.restaurantservice.entities;

import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.enums.Status;
import lombok.*;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SolrDocument(solrCoreName = "restaurants")
public class Restaurant {
    @Id
    @Field("id")
    @Indexed(name = "id", type = "string")
    private String id = UUID.randomUUID().toString();
    @Indexed(name = "name", type = "string")
    private String name;
    @Indexed(name = "phone_number", type = "string")
    private String phoneNumber;
    @Indexed(name = "address", type = "string")
    private String address;
    @Indexed(name = "state", type = "string")
    private State state;
    @Indexed(name = "status", type = "string")
    private Status status;
    @Indexed(name = "latitude", type = "double")
    private Double latitude;
    @Indexed(name = "longitude", type = "double")
    private Double longitude;
    @Indexed(name = "rating", type = "double")
    private Double rating;
}
