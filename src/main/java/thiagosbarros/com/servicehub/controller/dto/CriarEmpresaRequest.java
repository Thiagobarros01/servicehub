package thiagosbarros.com.servicehub.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CriarEmpresaRequest(
        @NotBlank String nome
) {
}
