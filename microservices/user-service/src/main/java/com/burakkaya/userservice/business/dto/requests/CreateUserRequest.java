package com.burakkaya.userservice.business.dto.requests;

import com.burakkaya.commonpackage.constants.Regex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Length(min = 2, max = 50)
    private String lastName;
    @NotBlank
    @Pattern(regexp = Regex.email)
    private String email;
    @NotBlank
    @Length(min = 6, max = 50)
    private String password;
    @NotBlank
    private String passwordConfirm;
    @NotBlank
    @Pattern(regexp = Regex.phoneNumber)
    private String phoneNumber;
    @NotBlank
    @Length(min = 10, max = 200)
    private String address;
    @NotBlank
    private Double latitude;
    @NotBlank
    private Double longitude;
}
