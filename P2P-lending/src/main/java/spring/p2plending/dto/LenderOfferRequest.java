package spring.p2plending.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LenderOfferRequest {
    private BigDecimal amount;
    private Double interestRate;
    private Integer termInMonths;
}

