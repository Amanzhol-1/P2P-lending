package spring.p2plending.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spring.p2plending.model.LogEntry;
import spring.p2plending.repository.LogEntryRepository;

import java.time.LocalDateTime;

@Service
public class LogService {

    private final LogEntryRepository logEntryRepository;

    @Autowired
    public LogService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @Async
    public void log(String level, String logger, String message, String thread, String exception) {
        LogEntry logEntry = LogEntry.builder()
                .timestamp(LocalDateTime.now())
                .level(level)
                .logger(logger)
                .message(message)
                .thread(thread)
                .exception(exception)
                .build();
        logEntryRepository.save(logEntry);
    }

    @Async
    public void log(String level, String logger, String message, String thread) {
        log(level, logger, message, thread, null);
    }
}


