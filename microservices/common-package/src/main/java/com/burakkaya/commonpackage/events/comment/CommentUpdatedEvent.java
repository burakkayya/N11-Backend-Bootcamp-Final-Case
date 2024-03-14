package com.burakkaya.commonpackage.events.comment;

import com.burakkaya.commonpackage.events.Event;
import com.burakkaya.commonpackage.utils.enums.Rate;

public class CommentUpdatedEvent implements Event {
    private String commentId;
    private String text;
    private Rate rate;
}
