package thiagosbarros.com.servicehub.controller.dto;

import java.time.Instant;

public record AgendamentoResponse(
        Long id,
        Long empresaId,
        Long clienteId,
        Long servicoId,
        Instant dataHoraInicio,
        String status
) {
}
