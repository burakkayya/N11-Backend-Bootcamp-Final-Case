package com.burakkaya.commonpackage.events.comment;

import com.burakkaya.commonpackage.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDeletedEvent implements Event {
    private String commentId;
}