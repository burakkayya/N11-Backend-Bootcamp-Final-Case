package com.burakkaya.logservice.business.abstracts;


import com.burakkaya.logservice.business.dto.requests.LogRequest;
import com.burakkaya.logservice.business.dto.responses.LogResponse;

import java.util.List;

public interface LogService {
    List<LogResponse> getAll();
    LogResponse createLog(LogRequest request);
}
