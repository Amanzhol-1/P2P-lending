package spring.p2plending.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.dto.OrderRequestDTO;
import spring.p2plending.dto.OrderResponseDTO;
import spring.p2plending.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Создание нового заказа на покупку товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заказ успешно создан",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные", content = @Content)
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO order = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
