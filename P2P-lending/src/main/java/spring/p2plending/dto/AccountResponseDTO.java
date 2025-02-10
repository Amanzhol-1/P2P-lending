package spring.p2plending.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.p2plending.enums.AccountStatus;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private BigDecimal balance;
    private String currency;
    private AccountStatus status;
}
