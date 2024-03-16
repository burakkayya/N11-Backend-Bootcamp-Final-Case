package com.burakkaya.commonpackage.configuration.exceptions;


import com.burakkaya.commonpackage.events.exception.ExceptionOccurredEvent;
import com.burakkaya.commonpackage.utils.constants.ExceptionTypes;
import com.burakkaya.commonpackage.utils.exceptions.BusinessException;
import com.burakkaya.commonpackage.utils.kafka.producer.KafkaProducer;
import com.burakkaya.commonpackage.utils.results.ExceptionResult;
import javax.validation.ValidationException;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler {

    private final KafkaProducer kafkaProducer;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ExceptionResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String,String> validationErrors = new HashMap<>();
        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()){
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
            sendKafkaExceptionOccurredEvent(ExceptionTypes.Exception.Validation, fieldError.getDefaultMessage());
        }
        return new ExceptionResult<>(ExceptionTypes.Exception.Validation,validationErrors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 422
    public ExceptionResult<Object> handleValidationException(ValidationException exception){
        sendKafkaExceptionOccurredEvent(ExceptionTypes.Exception.Validation, exception.getMessage());
        return new ExceptionResult<>(ExceptionTypes.Exception.Validation, exception.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 422
    public ExceptionResult<Object> handleBusinessException(BusinessException exception){
        sendKafkaExceptionOccurredEvent(ExceptionTypes.Exception.Business, exception.getMessage());
        return new ExceptionResult<>(ExceptionTypes.Exception.Business,exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public ExceptionResult<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception){
        sendKafkaExceptionOccurredEvent(ExceptionTypes.Exception.DataIntegrityViolation, exception.getMessage());
        return new ExceptionResult<>(ExceptionTypes.Exception.DataIntegrityViolation, exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class) // 500
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResult<Object> handleRuntimeException(RuntimeException exception){
        if(exception.getMessage().contains("Access Denied")){
            sendKafkaExceptionOccurredEvent(ExceptionTypes.Exception.AccessDenied, new AccessDeniedException("Access Denied").getMessage());
            return new ExceptionResult<>(ExceptionTypes.Exception.AccessDenied, new AccessDeniedException("Access Denied").getMessage());
        }
        sendKafkaExceptionOccurredEvent(ExceptionTypes.Exception.Runtime, exception.getMessage());
        return new ExceptionResult<>(ExceptionTypes.Exception.Runtime, exception.getMessage());
    }

    private void sendKafkaExceptionOccurredEvent(String type, String message) {
        var event = new ExceptionOccurredEvent(type, message, LocalDateTime.now());
        kafkaProducer.sendMessage(event, "exception-occurred");
    }
}