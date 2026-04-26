package thiagosbarros.com.servicehub.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CriarServicoRequest(
        @NotBlank String nome,
        @NotNull @Positive Integer duracaoMinutos,
        @NotNull @DecimalMin("0.0") BigDecimal preco
) {
}
