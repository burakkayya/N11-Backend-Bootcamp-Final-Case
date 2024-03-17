package com.burakkaya.commentservice.business.concretes;

import com.burakkaya.commentservice.business.dto.requests.CreateCommentRequest;
import com.burakkaya.commentservice.business.dto.requests.UpdateCommentRequest;
import com.burakkaya.commentservice.business.dto.responses.CreateCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.GetAllCommentsResponse;
import com.burakkaya.commentservice.business.dto.responses.GetCommentResponse;
import com.burakkaya.commentservice.business.dto.responses.UpdateCommentResponse;
import com.burakkaya.commentservice.business.rules.CommentBusinessRules;
import com.burakkaya.commentservice.entities.Comment;
import com.burakkaya.commentservice.repository.CommentRepository;
import com.burakkaya.commonpackage.events.comment.CommentCreatedEvent;
import com.burakkaya.commonpackage.events.comment.CommentDeletedEvent;
import com.burakkaya.commonpackage.events.comment.CommentUpdatedEvent;
import com.burakkaya.commonpackage.utils.enums.Rate;
import com.burakkaya.commonpackage.utils.kafka.producer.KafkaProducer;
import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentManagerTest {

    @Mock
    private CommentRepository mockCommentRepository;
    @Mock
    private ModelMapperService mockModelMapperService;
    @Mock
    private CommentBusinessRules mockRules;
    @Mock
    private KafkaProducer mockProducer;

    private CommentManager commentManagerUnderTest;

    @BeforeEach
    void setUp() {
        commentManagerUnderTest = new CommentManager(mockCommentRepository, mockModelMapperService, mockRules,
                mockProducer);
    }

    @Test
    void testGetAllComments() {
        final Comment comment = new Comment();
        comment.setId("id");
        comment.setUserId("userId");
        comment.setRestaurantId("restaurantId");
        comment.setRate(Rate.ONE);
        comment.setCommentedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Iterable<Comment> comments = List.of(comment);
        when(mockCommentRepository.findAll()).thenReturn(comments);

        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());

        final List<GetAllCommentsResponse> result = commentManagerUnderTest.getAllComments();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(mockModelMapperService, times(1)).forResponse();

        verify(mockCommentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllComments_CommentRepositoryReturnsNoItems() {
        when(mockCommentRepository.findAll()).thenReturn(Collections.emptyList());

        final List<GetAllCommentsResponse> result = commentManagerUnderTest.getAllComments();

        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetCommentById() {
        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());

        final Comment comment1 = new Comment();
        comment1.setId("id");
        comment1.setUserId("userId");
        comment1.setRestaurantId("restaurantId");
        comment1.setRate(Rate.ONE);
        comment1.setCommentedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Optional<Comment> comment = Optional.of(comment1);
        when(mockCommentRepository.findById("id")).thenReturn(comment);

        final GetCommentResponse result = commentManagerUnderTest.getCommentById("id");

        verify(mockRules).checkIfCommentExistsById("id");
    }

    @Test
    void testGetCommentById_CommentRepositoryReturnsAbsent() {
        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());
        when(mockCommentRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentManagerUnderTest.getCommentById("id"))
                .isInstanceOf(NoSuchElementException.class);
        verify(mockRules).checkIfCommentExistsById("id");
    }

    @Test
    void testCreateComment() {
        final CreateCommentRequest createCommentRequest = new CreateCommentRequest("text", "userId", "restaurantId",
                Rate.ONE);

        ModelMapper requestMapper = new ModelMapper();
        ModelMapper responseMapper = new ModelMapper();

        PropertyMap<CreateCommentRequest, Comment> commentMap = new PropertyMap<CreateCommentRequest, Comment>() {
            protected void configure() {
                map().setId(source.getUserId());
            }
        };

        requestMapper.addMappings(commentMap);
        responseMapper.addMappings(commentMap);

        when(mockModelMapperService.forRequest()).thenReturn(requestMapper);
        when(mockModelMapperService.forResponse()).thenReturn(responseMapper);

        when(mockCommentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setId("id");
            return savedComment;
        });

        final CreateCommentResponse result = commentManagerUnderTest.createComment(createCommentRequest);

        verify(mockRules).checkIfUserExists("userId");
        verify(mockRules).checkIfRestaurantExists("restaurantId");
        verify(mockProducer).sendMessage(any(CommentCreatedEvent.class), eq("comment-created"));
    }

    @Test
    void testCreateComment_CommentRepositoryThrowsOptimisticLockingFailureException() {
        final CreateCommentRequest createCommentRequest = new CreateCommentRequest("text", "userId", "restaurantId", Rate.ONE);

        ModelMapper requestMapper = new ModelMapper();
        ModelMapper responseMapper = new ModelMapper();

        PropertyMap<CreateCommentRequest, Comment> commentMap = new PropertyMap<CreateCommentRequest, Comment>() {
            protected void configure() {
                map().setId(source.getUserId());
            }
        };

        requestMapper.addMappings(commentMap);
        responseMapper.addMappings(commentMap);

        when(mockModelMapperService.forRequest()).thenReturn(requestMapper);
        when(mockModelMapperService.forResponse()).thenReturn(responseMapper);

        when(mockCommentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setId("id");
            return savedComment;
        }).thenThrow(OptimisticLockingFailureException.class);

        try {
            commentManagerUnderTest.createComment(createCommentRequest);
            fail("OptimisticLockingFailureException beklenmiyordu");
        } catch (OptimisticLockingFailureException e) {
            System.out.println("Beklenen OptimisticLockingFailureException fırlatıldı.");
        }
    }

    @Test
    void testUpdateComment() {
        final UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("id", "text", Rate.ONE);

        final Comment comment1 = new Comment();
        comment1.setId("id");
        comment1.setUserId("userId");
        comment1.setRestaurantId("restaurantId");
        comment1.setRate(Rate.ONE);
        comment1.setCommentedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Optional<Comment> comment = Optional.of(comment1);
        when(mockCommentRepository.findById("id")).thenReturn(comment);

        when(mockModelMapperService.forRequest()).thenReturn(new ModelMapper());

        final Comment comment2 = new Comment();
        comment2.setId("id");
        comment2.setUserId("userId");
        comment2.setRestaurantId("restaurantId");
        comment2.setRate(Rate.ONE);
        comment2.setCommentedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockCommentRepository.save(any(Comment.class))).thenReturn(comment2);

        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());

        final UpdateCommentResponse result = commentManagerUnderTest.updateComment("id", updateCommentRequest);

        verify(mockRules).checkIfCommentExistsById("id");
        verify(mockProducer).sendMessage(any(CommentUpdatedEvent.class), eq("comment-updated"));
    }

    @Test
    void testUpdateComment_CommentRepositoryFindByIdReturnsAbsent() {
        final UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("id", "text", Rate.ONE);
        when(mockCommentRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentManagerUnderTest.updateComment("id", updateCommentRequest))
                .isInstanceOf(NoSuchElementException.class);
        verify(mockRules).checkIfCommentExistsById("id");
    }

    @Test
    void testUpdateComment_CommentRepositorySaveThrowsOptimisticLockingFailureException() {
        final UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("id", "text", Rate.ONE);

        final Comment comment1 = new Comment();
        comment1.setId("id");
        comment1.setUserId("userId");
        comment1.setRestaurantId("restaurantId");
        comment1.setRate(Rate.ONE);
        comment1.setCommentedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Optional<Comment> comment = Optional.of(comment1);
        when(mockCommentRepository.findById("id")).thenReturn(comment);

        when(mockModelMapperService.forRequest()).thenReturn(new ModelMapper());
        when(mockCommentRepository.save(any(Comment.class))).thenThrow(OptimisticLockingFailureException.class);

        assertThatThrownBy(() -> commentManagerUnderTest.updateComment("id", updateCommentRequest))
                .isInstanceOf(OptimisticLockingFailureException.class);
        verify(mockRules).checkIfCommentExistsById("id");
    }

    @Test
    void testDeleteComment() {
        final Comment comment1 = new Comment();
        comment1.setId("id");
        comment1.setUserId("userId");
        comment1.setRestaurantId("restaurantId");
        comment1.setRate(Rate.ONE);
        comment1.setCommentedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Optional<Comment> comment = Optional.of(comment1);
        when(mockCommentRepository.findById("id")).thenReturn(comment);

        when(mockModelMapperService.forResponse()).thenReturn(new ModelMapper());

        commentManagerUnderTest.deleteComment("id");

        verify(mockRules).checkIfCommentExistsById("id");
        verify(mockProducer).sendMessage(any(CommentDeletedEvent.class), eq("comment-deleted"));
        verify(mockCommentRepository).deleteById("id");
    }

    @Test
    void testDeleteComment_CommentRepositoryFindByIdReturnsAbsent() {
        when(mockCommentRepository.findById("id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentManagerUnderTest.deleteComment("id"))
                .isInstanceOf(NoSuchElementException.class);
        verify(mockRules).checkIfCommentExistsById("id");
    }
}
