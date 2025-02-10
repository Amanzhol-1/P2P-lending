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
import spring.p2plending.dto.TransactionRequestDTO;
import spring.p2plending.dto.TransactionResponseDTO;
import spring.p2plending.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Создание новой транзакции")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Транзакция успешно создана",
                    content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO transactionRequest) {
        TransactionResponseDTO created = transactionService.createTransaction(transactionRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}

