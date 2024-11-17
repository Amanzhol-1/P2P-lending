package spring.p2plending.сontroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.dto.BalanceRequest;
import spring.p2plending.model.User;
import spring.p2plending.security.CustomUserDetails;
import spring.p2plending.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Top up user balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance topped up successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content)
    })
    @PostMapping("/balance/top-up")
    public ResponseEntity<?> topUpBalance(@RequestBody BalanceRequest balanceRequest,
                                          Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findByNickname(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.updateUserBalance(user, balanceRequest.getAmount());

        return ResponseEntity.ok("Баланс успешно пополнен.");
    }

    @Operation(summary = "Get current user balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current balance retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content)
    })
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findByNickname(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok("Ваш текущий баланс: " + user.getBalance());
    }
}
