package spring.p2plending.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {
    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Product ID is required")
    private Long productId;
}
