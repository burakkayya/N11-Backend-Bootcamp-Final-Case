package com.burakkaya.logservice.business.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
    private String id;
    private String type;
    private String message;
    private LocalDateTime timestamp;
}
