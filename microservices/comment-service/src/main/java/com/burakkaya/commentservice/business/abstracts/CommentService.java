package com.burakkaya.commentservice.business.abstracts;

import com.burakkaya.commentservice.business.dto.requests.CreateCommentRequest;
import com.burakkaya.commentservice.business.dto.requests.UpdateCommentRequest;
import com.burakkaya.commentservice.business.dto.responses.CreateCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.GetAllCommentsResponse;
import com.burakkaya.commentservice.business.dto.responses.GetCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.UpdateCommentResponse;

import java.util.List;

public interface CommentService {
    List<GetAllCommentsResponse> getAllComments();
    GetCommentResponse getCommentById(String id);
    CreateCommentResponse createComment(CreateCommentRequest createCommentRequest);
    UpdateCommentResponse updateComment(String id, UpdateCommentRequest updateCommentRequest);
    void deleteComment(String id);
}
