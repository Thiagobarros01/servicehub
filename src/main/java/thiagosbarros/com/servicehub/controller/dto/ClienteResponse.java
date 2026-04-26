package thiagosbarros.com.servicehub.controller.dto;

import java.time.LocalDate;

public record ClienteResponse(
        Long id,
        Long empresaId,
        String nome,
        String email,
        LocalDate dataNascimento
) {
}
