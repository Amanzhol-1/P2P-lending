package spring.p2plending.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.p2plending.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private Long accountId;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description;
}
