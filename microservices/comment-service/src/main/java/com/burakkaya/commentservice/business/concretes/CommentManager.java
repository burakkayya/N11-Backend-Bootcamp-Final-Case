package com.burakkaya.commentservice.business.concretes;

import com.burakkaya.commentservice.business.abstracts.CommentService;
import com.burakkaya.commentservice.business.dto.requests.CreateCommentRequest;
import com.burakkaya.commentservice.business.dto.requests.UpdateCommentRequest;
import com.burakkaya.commentservice.business.dto.responses.CreateCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.GetAllCommentsResponse;
import com.burakkaya.commentservice.business.dto.responses.GetCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.UpdateCommentResponse;
import com.burakkaya.commentservice.business.rules.CommentBusinessRules;
import com.burakkaya.commentservice.entities.Comment;
import com.burakkaya.commentservice.repository.CommentRepository;
import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentManager implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapperService modelMapperService;
    private final CommentBusinessRules rules;

    @Override
    public List<GetAllCommentsResponse> getAllComments() {
        List<GetAllCommentsResponse> responses = new ArrayList<>();
        commentRepository.findAll().forEach(comment ->  {
            GetAllCommentsResponse response = modelMapperService.forResponse().map(comment, GetAllCommentsResponse.class);
            responses.add(response);
        });
        return responses;
    }

    @Override
    public GetCommentResponse getCommentById(String id) {
        rules.checkIfCommentExistsById(id);
        GetCommentResponse response = modelMapperService.forResponse().map(commentRepository.findById(id).orElseThrow(), GetCommentResponse.class);
        return response;
    }

    @Override
    public CreateCommentResponse createComment(CreateCommentRequest createCommentRequest) {
        rules.checkIfUserExists(createCommentRequest.getUserId());
        rules.checkIfRestaurantExists(createCommentRequest.getRestaurantId());
        Comment comment = modelMapperService.forRequest().map(createCommentRequest, Comment.class);
        Comment createdComment = commentRepository.save(comment);
        CreateCommentResponse response = modelMapperService.forResponse().map(createdComment, CreateCommentResponse.class);
        return response;
    }

    @Override
    public UpdateCommentResponse updateComment(String id, UpdateCommentRequest updateCommentRequest) {
        rules.checkIfCommentExistsById(id);
        Comment comment = modelMapperService.forRequest().map(updateCommentRequest, Comment.class);
        comment.setId(id);
        Comment updatedComment = commentRepository.save(comment);
        UpdateCommentResponse response = modelMapperService.forResponse().map(updatedComment, UpdateCommentResponse.class);
        return response;
    }

    @Override
    public void deleteComment(String id) {
        rules.checkIfCommentExistsById(id);
        commentRepository.deleteById(id);
    }
}
