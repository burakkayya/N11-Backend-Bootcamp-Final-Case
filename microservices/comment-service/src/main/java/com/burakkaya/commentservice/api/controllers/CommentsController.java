package com.burakkaya.commentservice.api.controllers;

import com.burakkaya.commentservice.business.abstracts.CommentService;
import com.burakkaya.commentservice.business.dto.requests.CreateCommentRequest;
import com.burakkaya.commentservice.business.dto.requests.UpdateCommentRequest;
import com.burakkaya.commentservice.business.dto.responses.CreateCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.GetAllCommentsResponse;
import com.burakkaya.commentservice.business.dto.responses.GetCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.UpdateCommentResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
public class CommentsController {
    private final CommentService commentService;

    @GetMapping
    public List<GetAllCommentsResponse> getAll(){
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public GetCommentResponse getById(@PathVariable String id){
        return commentService.getCommentById(id);
    }

    @PostMapping
    public CreateCommentResponse add(@Valid @RequestBody CreateCommentRequest request){
        return commentService.createComment(request);
    }

    @PutMapping("/{id}")
    public UpdateCommentResponse update(@PathVariable String id, @Valid @RequestBody UpdateCommentRequest request){
        return commentService.updateComment(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        commentService.deleteComment(id);
    }
}
