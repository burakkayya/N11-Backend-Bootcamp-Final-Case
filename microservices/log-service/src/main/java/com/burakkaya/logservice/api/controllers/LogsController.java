package com.burakkaya.logservice.api.controllers;

import com.burakkaya.logservice.business.abstracts.LogService;
import com.burakkaya.logservice.business.dto.responses.LogResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/logs")
public class LogsController {

    private final LogService service;

    @GetMapping
    public List<LogResponse> getAll(){
        return service.getAll();
    }
}
