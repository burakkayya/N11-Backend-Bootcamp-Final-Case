package com.burakkaya.commonpackage.utils.dto;

import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllRestaurantResponse {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private State state;
    private Status status;
    private Double latitude;
    private Double longitude;
    private Double rating;
}
