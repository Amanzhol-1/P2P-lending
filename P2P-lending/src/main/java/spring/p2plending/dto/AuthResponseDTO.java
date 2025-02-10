package spring.p2plending.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    @NonNull
    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
}
