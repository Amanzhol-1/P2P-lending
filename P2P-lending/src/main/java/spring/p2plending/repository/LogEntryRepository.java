package spring.p2plending.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import spring.p2plending.model.LogEntry;

@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, String> {
}

