package spring.p2plending.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.dto.ProductRequestDTO;
import spring.p2plending.dto.ProductResponseDTO;
import spring.p2plending.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Получение списка доступных товаров")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список товаров получен",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class)))),
            @ApiResponse(responseCode = "204", description = "Нет доступных товаров", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        List<ProductResponseDTO> products = productService.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Создание нового товара")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Товар успешно создан",
                    content = @Content(schema = @Schema(implementation = ProductRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productDTO) {
        ProductResponseDTO created = productService.createProduct(productDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}

