package spring.p2plending.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;


@Document(collection = "logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogEntry implements Serializable {

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
}

