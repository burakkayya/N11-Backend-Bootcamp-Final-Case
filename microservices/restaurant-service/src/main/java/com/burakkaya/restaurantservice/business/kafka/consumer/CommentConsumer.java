package com.burakkaya.restaurantservice.business.kafka.consumer;

import com.burakkaya.commonpackage.events.comment.CommentCreatedEvent;
import com.burakkaya.commonpackage.events.comment.CommentDeletedEvent;
import com.burakkaya.commonpackage.events.comment.CommentUpdatedEvent;
import com.burakkaya.restaurantservice.business.abstracts.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentConsumer {

    private final RestaurantService restaurantService;

    @KafkaListener(
            topics =  "comment-created",
            groupId = "restaurant-comment-create"
    )
    public void consume(CommentCreatedEvent event){
        restaurantService.updateRestaurantRatingWhenCommentCreated(event.getRestaurantId(), event.getRate().getValue());
        log.info("Comment created event consumed {}", event);
    }

    @KafkaListener(
            topics =  "comment-updated",
            groupId = "restaurant-comment-updated"
    )
    public void consume(CommentUpdatedEvent event){
        restaurantService.updateRestaurantRatingWhenCommentUpdated(event.getRestaurantId(), event.getOldRate().getValue(), event.getNewRate().getValue());
        log.info("Comment updated event consumed {}", event);
    }

    @KafkaListener(
            topics =  "comment-deleted",
            groupId = "restaurant-comment-deleted"
    )
    public void consume(CommentDeletedEvent event){
        restaurantService.updateRestaurantRatingWhenCommentDeleted(event.getRestaurantId(), event.getRate().getValue());
        log.info("Comment deleted event consumed {}", event);
    }
}
