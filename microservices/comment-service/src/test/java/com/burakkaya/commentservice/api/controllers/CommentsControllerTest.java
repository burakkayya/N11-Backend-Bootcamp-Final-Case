package com.burakkaya.commentservice.api.controllers;

import com.burakkaya.commentservice.business.abstracts.CommentService;
import com.burakkaya.commentservice.business.dto.requests.CreateCommentRequest;
import com.burakkaya.commentservice.business.dto.requests.UpdateCommentRequest;
import com.burakkaya.commentservice.business.dto.responses.CreateCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.GetAllCommentsResponse;
import com.burakkaya.commentservice.business.dto.responses.GetCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.UpdateCommentResponse;
import com.burakkaya.commonpackage.utils.enums.Rate;
import com.burakkaya.commonpackage.utils.kafka.producer.KafkaProducer;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentsController.class)
class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService mockCommentService;

    @MockBean
    private KafkaProducer kafkaProducer;

    @Test
    void testGetAll() throws Exception {
        final GetAllCommentsResponse getAllCommentsResponse = new GetAllCommentsResponse();
        getAllCommentsResponse.setId("id");
        getAllCommentsResponse.setText("text");
        getAllCommentsResponse.setUserId("userId");
        getAllCommentsResponse.setRestaurantId("restaurantId");
        getAllCommentsResponse.setRate(Rate.ONE);
        getAllCommentsResponse.setCommentedAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        final List<GetAllCommentsResponse> getAllCommentsResponses = List.of(getAllCommentsResponse);
        when(mockCommentService.getAllComments()).thenReturn(getAllCommentsResponses);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                "[" +
                "{" +
                "\"id\":\"id\"," +
                "\"text\":\"text\"," +
                "\"userId\":\"userId\"," +
                "\"restaurantId\":\"restaurantId\"," +
                "\"rate\":\"ONE\"," + "\"commentedAt\":\"2021-01-01T01:01:00\"" +
                "}" +
                "]");
    }

    @Test
    void testGetAll_CommentServiceReturnsNoItems() throws Exception {
        when(mockCommentService.getAllComments()).thenReturn(Collections.emptyList());

        final MockHttpServletResponse response = mockMvc.perform(get("/api/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    void testGetById() throws Exception {
        final GetCommentResponse getCommentResponse = new GetCommentResponse();
        getCommentResponse.setId("id");
        getCommentResponse.setText("text");
        getCommentResponse.setUserId("userId");
        getCommentResponse.setRestaurantId("restaurantId");
        getCommentResponse.setRate(Rate.ONE);
        getCommentResponse.setCommentedAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        when(mockCommentService.getCommentById("id")).thenReturn(getCommentResponse);

        final MockHttpServletResponse response = mockMvc.perform(get("/api/comments/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                "{" +
                "\"id\":\"id\"," +
                "\"text\":\"text\"," +
                "\"userId\":\"userId\"," +
                "\"restaurantId\":\"restaurantId\"," +
                "\"rate\":\"ONE\"," + "\"commentedAt\":\"2021-01-01T01:01:00\"" +
                "}");
    }

    @Test
    void testAdd() throws Exception {
        final CreateCommentRequest createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setText("some comment text");
        createCommentRequest.setUserId("userId");
        createCommentRequest.setRestaurantId("restaurantId");
        createCommentRequest.setRate(Rate.ONE);

        final CreateCommentResponse createCommentResponse = new CreateCommentResponse();
        createCommentResponse.setId("id");
        createCommentResponse.setText("some comment text");
        createCommentResponse.setUserId("userId");
        createCommentResponse.setRestaurantId("restaurantId");
        createCommentResponse.setRate(Rate.ONE);
        createCommentResponse.setCommentedAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        when(mockCommentService.createComment(any(CreateCommentRequest.class))).thenReturn(createCommentResponse);

        final MockHttpServletResponse response = mockMvc.perform(post("/api/comments")
                        .content(new ObjectMapper().writeValueAsString(createCommentRequest)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{" +
                "\"id\":\"id\"," +
                "\"text\":\"some comment text\"," +
                "\"userId\":\"userId\"," +
                "\"restaurantId\":\"restaurantId\"," +
                "\"rate\":\"ONE\"," + "\"commentedAt\":\"2021-01-01T01:01:00\"" +
                "}");
    }

    @Test
    void testUpdate() throws Exception {

        final UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.setId("id");
        updateCommentRequest.setText("some comment text");
        updateCommentRequest.setRate(Rate.ONE);

        final UpdateCommentResponse updateCommentResponse = new UpdateCommentResponse();
        updateCommentResponse.setId("id");
        updateCommentResponse.setText("some comment text");
        updateCommentResponse.setUserId("userId");
        updateCommentResponse.setRestaurantId("restaurantId");
        updateCommentResponse.setRate(Rate.ONE);
        updateCommentResponse.setCommentedAt(LocalDateTime.of(2021, 1, 1, 1, 1));

        when(mockCommentService.updateComment(eq("id"), any(UpdateCommentRequest.class)))
                .thenReturn(updateCommentResponse);

        final MockHttpServletResponse response = mockMvc.perform(put("/api/comments/{id}", "id")
                        .content(new ObjectMapper().writeValueAsString(updateCommentRequest)).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{" +
                "\"id\":\"id\"," +
                "\"text\":\"some comment text\"," +
                "\"userId\":\"userId\"," +
                "\"restaurantId\":\"restaurantId\"," +
                "\"rate\":\"ONE\"," + "\"commentedAt\":\"2021-01-01T01:01:00\"" +
                "}");
    }

    @Test
    void testDelete() throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(delete("/api/comments/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
        verify(mockCommentService).deleteComment("id");
    }
}
