package spring.p2plending.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;


@Document(collection = "logs")
@Data
public class LogEntry {

    @Id
    private String id;

    @NotNull
    private LocalDateTime timestamp;

    @NotBlank
    private String level;

    @NotBlank
    private String logger;

    @NotBlank
    private String message;

    @NotBlank
    private String thread;

    private String exception;

    public LogEntry(LocalDateTime timestamp, String level, String logger, String message, String thread, String exception) {
        this.timestamp = timestamp;
        this.level = level;
        this.logger = logger;
        this.message = message;
        this.thread = thread;
        this.exception = exception;
    }
}

