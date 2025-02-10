package spring.p2plending.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceUpdateRequestDTO {
    @NotNull(message = "New balance cannot be null")
    private BigDecimal newBalance;
}
