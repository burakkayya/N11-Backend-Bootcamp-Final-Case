package com.burakkaya.logservice.business.concretes;

import com.burakkaya.commonpackage.utils.mappers.ModelMapperService;
import com.burakkaya.logservice.business.abstracts.LogService;
import com.burakkaya.logservice.business.dto.requests.LogRequest;
import com.burakkaya.logservice.business.dto.responses.LogResponse;
import com.burakkaya.logservice.entities.Log;
import com.burakkaya.logservice.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LogManager implements LogService {

    private final LogRepository repository;
    private final ModelMapperService mapper;

    @Override
    public List<LogResponse> getAll() {
        List<Log> logs = repository.findAll();
        List<LogResponse> response = logs
                .stream()
                .map(log -> mapper.forResponse().map(log, LogResponse.class))
                .toList();
        return response;
    }

    @Override
    public LogResponse createLog(LogRequest request) {
        Log log = mapper.forRequest().map(request, Log.class);
        log.setId(null);
        var createdLog = repository.save(log);
        LogResponse response = mapper.forResponse().map(createdLog, LogResponse.class);
        return response;
    }
}
