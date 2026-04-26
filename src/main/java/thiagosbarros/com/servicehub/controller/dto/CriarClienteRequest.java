package thiagosbarros.com.servicehub.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CriarClienteRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        LocalDate dataNascimento
) {
}
