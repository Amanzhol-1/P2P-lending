package spring.p2plending.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
