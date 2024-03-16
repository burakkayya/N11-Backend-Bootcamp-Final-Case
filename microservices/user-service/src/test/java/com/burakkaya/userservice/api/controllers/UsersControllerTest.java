package com.burakkaya.userservice.api.controllers;

import com.burakkaya.commonpackage.utils.dto.ClientResponse;
import com.burakkaya.commonpackage.utils.enums.Status;
import com.burakkaya.commonpackage.utils.kafka.producer.KafkaProducer;
import com.burakkaya.userservice.business.abstracts.UserService;
import com.burakkaya.userservice.business.dto.requests.CreateUserRequest;
import com.burakkaya.userservice.business.dto.requests.UpdateUserRequest;
import com.burakkaya.userservice.business.dto.responses.CreateUserResponse;
import com.burakkaya.userservice.business.dto.responses.GetAllUsersResponse;
import com.burakkaya.userservice.business.dto.responses.GetUserResponse;
import com.burakkaya.userservice.business.dto.responses.UpdateUserResponse;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService mockService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void testGetAll() throws Exception {
        final GetAllUsersResponse getAllUsersResponse = new GetAllUsersResponse();
        getAllUsersResponse.setId(UUID.fromString("e4653736-0adf-49f3-8781-d8894c04838a"));
        getAllUsersResponse.setFirstName("firstName");
        getAllUsersResponse.setLastName("lastName");
        getAllUsersResponse.setEmail("valid_email@example.com");
        getAllUsersResponse.setPassword("password");
        getAllUsersResponse.setPhoneNumber("1234567890");
        getAllUsersResponse.setAddress("valid address");
        getAllUsersResponse.setLongitude(0.0);
        getAllUsersResponse.setLatitude(0.0);
        getAllUsersResponse.setStatus(Status.ACTIVE);

        final List<GetAllUsersResponse> getAllUsersResponses = List.of(getAllUsersResponse);
        when(mockService.getAllUsers()).thenReturn(getAllUsersResponses);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualToIgnoringWhitespace("[{"
                        + "\"id\":\"e4653736-0adf-49f3-8781-d8894c04838a\","
                        + "\"firstName\":\"firstName\","
                        + "\"lastName\":\"lastName\","
                        + "\"email\":\"valid_email@example.com\","
                        + "\"password\":\"password\","
                        + "\"phoneNumber\":\"1234567890\","
                        + "\"address\":\"valid address\","
                        + "\"status\":\"ACTIVE\","
                        + "\"latitude\":0.0,"
                        + "\"longitude\":0.0"
                        + "}]");
    }

    @Test
    void testGetAll_UserServiceReturnsNoItems() throws Exception {
        when(mockService.getAllUsers()).thenReturn(Collections.emptyList());

        final MockHttpServletResponse response = mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetById() throws Exception {
        final GetUserResponse getUserResponse = new GetUserResponse();
        getUserResponse.setId(UUID.fromString("e4653736-0adf-49f3-8781-d8894c04838a"));
        getUserResponse.setFirstName("firstName");
        getUserResponse.setLastName("lastName");
        getUserResponse.setEmail("valid_email@example.com");
        getUserResponse.setPassword("password");
        getUserResponse.setPhoneNumber("1234567890");
        getUserResponse.setAddress("valid address");
        getUserResponse.setLongitude(0.0);
        getUserResponse.setLatitude(0.0);
        getUserResponse.setStatus(Status.ACTIVE);

        when(mockService.getUserById(UUID.fromString("e4653736-0adf-49f3-8781-d8894c04838a")))
                .thenReturn(getUserResponse);

        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/users/{id}", "e4653736-0adf-49f3-8781-d8894c04838a")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualToIgnoringWhitespace("{"
                        + "\"id\":\"e4653736-0adf-49f3-8781-d8894c04838a\","
                        + "\"firstName\":\"firstName\","
                        + "\"lastName\":\"lastName\","
                        + "\"email\":\"valid_email@example.com\","
                        + "\"password\":\"password\","
                        + "\"phoneNumber\":\"1234567890\","
                        + "\"address\":\"valid address\","
                        + "\"status\":\"ACTIVE\","
                        + "\"latitude\":0.0,"
                        + "\"longitude\":0.0"
                        + "}");
    }

    @Test
    void testAdd() throws Exception {

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setFirstName("firstName");
        createUserRequest.setLastName("lastName");
        createUserRequest.setEmail("valid_email@example.com");
        createUserRequest.setPassword("password");
        createUserRequest.setPasswordConfirm("password");
        createUserRequest.setPhoneNumber("1234567890");
        createUserRequest.setAddress("valid address");
        createUserRequest.setLongitude(0.0);
        createUserRequest.setLatitude(0.0);

        final CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(UUID.fromString("031b5bd7-6f1e-4a60-9806-46c5f4b43b18"));
        createUserResponse.setFirstName("firstName");
        createUserResponse.setLastName("lastName");
        createUserResponse.setEmail("valid_email@example.com");
        createUserResponse.setPassword("password");
        createUserResponse.setPhoneNumber("1234567890");
        createUserResponse.setAddress("valid address");
        createUserResponse.setLongitude(0.0);
        createUserResponse.setLatitude(0.0);
        createUserResponse.setStatus(Status.ACTIVE);

        when(mockService.createUser(any(CreateUserRequest.class))).thenReturn(createUserResponse);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/users")
                        .content(new ObjectMapper().writeValueAsString(createUserRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualToIgnoringWhitespace("{"
                        + "\"id\":\"031b5bd7-6f1e-4a60-9806-46c5f4b43b18\","
                        + "\"firstName\":\"firstName\","
                        + "\"lastName\":\"lastName\","
                        + "\"email\":\"valid_email@example.com\","
                        + "\"password\":\"password\","
                        + "\"phoneNumber\":\"1234567890\","
                        + "\"address\":\"valid address\","
                        + "\"status\":\"ACTIVE\","
                        + "\"latitude\":0.0,"
                        + "\"longitude\":0.0"
                        + "}");
    }

    @Test
    void testUpdate() throws Exception {
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(UUID.fromString("031b5bd7-6f1e-4a60-9806-46c5f4b43b18"));
        updateUserRequest.setFirstName("firstName");
        updateUserRequest.setLastName("lastName");
        updateUserRequest.setEmail("valid_email@example.com");
        updateUserRequest.setPhoneNumber("1234567890");
        updateUserRequest.setAddress("valid address");
        updateUserRequest.setLongitude(0.0);
        updateUserRequest.setLatitude(0.0);
        updateUserRequest.setStatus(Status.ACTIVE);

        final UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setId(UUID.fromString("031b5bd7-6f1e-4a60-9806-46c5f4b43b18"));
        updateUserResponse.setFirstName("firstName");
        updateUserResponse.setLastName("lastName");
        updateUserResponse.setEmail("valid_email@example.com");
        updateUserResponse.setPhoneNumber("1234567890");
        updateUserResponse.setAddress("valid address");
        updateUserResponse.setLongitude(0.0);
        updateUserResponse.setLatitude(0.0);
        updateUserResponse.setStatus(Status.ACTIVE);

        when(mockService.updateUser(eq(UUID.fromString("62fb5fed-95fd-4a5b-bf7e-f2965720c737")),
                any(UpdateUserRequest.class))).thenReturn(updateUserResponse);

        final MockHttpServletResponse response = mockMvc.perform(
                        put("/api/users/{id}", "62fb5fed-95fd-4a5b-bf7e-f2965720c737")
                                .content(new ObjectMapper().writeValueAsString(updateUserRequest)).contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualToIgnoringWhitespace("{"
                        + "\"id\":\"031b5bd7-6f1e-4a60-9806-46c5f4b43b18\","
                        + "\"firstName\":\"firstName\","
                        + "\"lastName\":\"lastName\","
                        + "\"email\":\"valid_email@example.com\","
                        + "\"password\":null,"
                        + "\"phoneNumber\":\"1234567890\","
                        + "\"address\":\"valid address\","
                        + "\"status\":\"ACTIVE\","
                        + "\"latitude\":0.0,"
                        + "\"longitude\":0.0"
                        + "}");
    }

    @Test
    void testDelete() throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(
                        delete("/api/users/{id}", "0f093f71-938e-4ab6-ab66-9b8b855746fb")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
        verify(mockService).deleteUser(UUID.fromString("0f093f71-938e-4ab6-ab66-9b8b855746fb"));
    }

    @Test
    void testCheckIfUserExists() throws Exception {
        when(mockService.checkIfUserExists(UUID.fromString("1c7f099c-cd21-49a2-9fa4-0aea81030823")))
                .thenReturn(new ClientResponse(false, "message"));

        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/users/check-user-exists/{id}", "1c7f099c-cd21-49a2-9fa4-0aea81030823")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{\"message\":\"message\",\"success\":false}");
    }
}
