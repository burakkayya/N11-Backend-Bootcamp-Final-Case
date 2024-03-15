package com.burakkaya.commonpackage.events.comment;

import com.burakkaya.commonpackage.events.Event;
import com.burakkaya.commonpackage.utils.enums.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdatedEvent implements Event {
    private String commentId;
    private String restaurantId;
    private Rate oldRate;
    private Rate newRate;
}
