package com.burakkaya.restaurantservice.business.dto.responses;

import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetRestaurantResponse {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private State state;
    private Status status;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private Integer commentCount;
}
