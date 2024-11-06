package spring.p2plending.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceRequest {
    private BigDecimal amount;
}
