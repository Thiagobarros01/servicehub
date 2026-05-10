package thiagosbarros.com.servicehub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull Long empresaId,
        @NotBlank String email,
        @NotBlank String senha
) {
}
