package thiagosbarros.com.servicehub.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CriarAgendamentoRequest(
        @NotNull Long clienteId,
        @NotNull Long servicoId,
        @NotNull @Future Instant dataHoraInicio
) {
}
