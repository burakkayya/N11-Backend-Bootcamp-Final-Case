package com.burakkaya.restaurantservice.business.dto.requests;

import com.burakkaya.commonpackage.utils.constants.Regex;
import com.burakkaya.commonpackage.utils.enums.State;
import com.burakkaya.commonpackage.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRestaurantRequest {
    @NotBlank
    private String id;
    @NotBlank
    @Length(min = 2, max = 50)
    private String name;
    @NotBlank
    @Pattern(regexp = Regex.phoneNumber)
    private String phoneNumber;
    @NotBlank
    @Length(min = 10, max = 200)
    private String address;
    @NotNull
    private State state;
    @NotNull
    private Status status;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
