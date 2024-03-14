package com.burakkaya.commentservice.repository;

import com.burakkaya.commentservice.entities.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface CommentRepository extends ElasticsearchRepository<Comment, String> {
}
