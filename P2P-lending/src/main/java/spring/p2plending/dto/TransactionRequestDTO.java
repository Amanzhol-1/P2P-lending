package spring.p2plending.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import spring.p2plending.enums.TransactionType;

@Data
public class TransactionRequestDTO {
    @NotNull(message = "Account ID is required")
    private Long accountId;

    // Если не передано, можно установить значение по умолчанию (например, DEBIT) в сервисе
    private TransactionType type;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    // опционально
    private String description;
}
