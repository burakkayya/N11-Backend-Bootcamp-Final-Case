package com.burakkaya.logservice.repository;

import com.burakkaya.logservice.entities.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {
}
