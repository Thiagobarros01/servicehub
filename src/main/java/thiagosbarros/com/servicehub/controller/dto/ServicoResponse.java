package thiagosbarros.com.servicehub.controller.dto;

import java.math.BigDecimal;

public record ServicoResponse(
        Long id,
        Long empresaId,
        String nome,
        Integer duracaoMinutos,
        BigDecimal preco
) {
}
