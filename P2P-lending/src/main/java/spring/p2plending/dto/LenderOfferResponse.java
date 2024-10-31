package spring.p2plending.dto;

import lombok.Data;
import spring.p2plending.enums.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LenderOfferResponse {
    private Long id;
    private BigDecimal amount;
    private Double interestRate;
    private Integer termInMonths;
    private OfferStatus status;
    private LocalDateTime createdAt;
    private Long lenderId;
    private String lenderNickname;
}
