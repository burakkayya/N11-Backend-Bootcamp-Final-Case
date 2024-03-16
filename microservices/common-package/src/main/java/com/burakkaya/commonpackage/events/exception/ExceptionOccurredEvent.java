package com.burakkaya.commonpackage.events.exception;

import com.burakkaya.commonpackage.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOccurredEvent implements Event{
    private String type;
    private String message;
    private LocalDateTime timestamp;
}
