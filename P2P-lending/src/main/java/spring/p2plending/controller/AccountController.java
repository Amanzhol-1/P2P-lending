package spring.p2plending.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.dto.AccountBalanceUpdateRequestDTO;
import spring.p2plending.dto.AccountResponseDTO;
import spring.p2plending.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Получение информации об аккаунте по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт найден",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Аккаунт не найден", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccount(@PathVariable Long id) {
        AccountResponseDTO account = accountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Обновление баланса аккаунта",
            description = "Принимает JSON объект с полем newBalance. Например: { \"newBalance\": 1500.00 }")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Баланс обновлен",
                    content = @Content(schema = @Schema(implementation = AccountResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Аккаунт не найден", content = @Content)
    })
    @PutMapping("/{id}/balance")
    public ResponseEntity<AccountResponseDTO> updateBalance(
            @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody AccountBalanceUpdateRequestDTO balanceUpdateRequest) {
        AccountResponseDTO updatedAccount = accountService.updateBalance(id, balanceUpdateRequest.getNewBalance());
        return ResponseEntity.ok(updatedAccount);
    }
}

