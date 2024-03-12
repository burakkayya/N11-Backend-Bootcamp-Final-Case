package com.burakkaya.userservice.business.dto.responses;

import com.burakkaya.commonpackage.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private Status status;
    private Double latitude;
    private Double longitude;
}
