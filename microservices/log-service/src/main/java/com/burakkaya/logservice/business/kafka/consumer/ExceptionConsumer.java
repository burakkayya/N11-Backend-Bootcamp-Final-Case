package com.burakkaya.logservice.business.kafka.consumer;

import com.burakkaya.commonpackage.events.comment.CommentCreatedEvent;
import com.burakkaya.commonpackage.events.exception.ExceptionOccurredEvent;
import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import com.burakkaya.logservice.business.abstracts.LogService;
import com.burakkaya.logservice.business.dto.requests.LogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExceptionConsumer {

    private final LogService logService;
    private final ModelMapperService mapper;
    @KafkaListener(
            topics =  "exception-occurred",
            groupId = "log-exception-occurred"
    )
    public void consume(ExceptionOccurredEvent event){
        LogRequest request = mapper.forRequest().map(event, LogRequest.class);
        logService.createLog(request);
        log.info("Exception occurred event consumed {}", event);
    }
}
