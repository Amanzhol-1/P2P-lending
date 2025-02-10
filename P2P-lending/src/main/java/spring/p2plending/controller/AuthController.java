package spring.p2plending.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.p2plending.dto.AuthResponseDTO;
import spring.p2plending.dto.LoginRequestDTO;
import spring.p2plending.dto.RegisterRequestDTO;
import spring.p2plending.model.User;
import spring.p2plending.security.JwtUtils;
import spring.p2plending.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован", content = @Content),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterRequestDTO registerRequest) {
        User user = userService.registerUser(registerRequest);
        return ResponseEntity.ok("Пользователь зарегистрирован: " + user.getUsername());
    }

    @Operation(summary = "Аутентификация пользователя и получение JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аутентификация успешна",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken((spring.p2plending.security.CustomUserDetails) authentication.getPrincipal());
        return ResponseEntity.ok(AuthResponseDTO.builder().accessToken(jwt).build());
    }
}

