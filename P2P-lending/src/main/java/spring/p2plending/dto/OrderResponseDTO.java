package spring.p2plending.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.p2plending.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Long accountId;
    private Long productId;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDateTime timestamp;
}
