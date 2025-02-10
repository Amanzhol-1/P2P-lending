package spring.p2plending.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    @NotBlank(message = "Product name is required")
    private String name;

    // Описание продукта может быть пустым
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    // Поле available можно задать, если клиент сам определяет доступность,
    // иначе сервер может установить значение по умолчанию
    private Boolean available;
}
