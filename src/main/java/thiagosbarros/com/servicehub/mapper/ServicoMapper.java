package thiagosbarros.com.servicehub.mapper;

import org.springframework.stereotype.Component;
import thiagosbarros.com.servicehub.controller.dto.CriarServicoRequest;
import thiagosbarros.com.servicehub.controller.dto.ServicoResponse;
import thiagosbarros.com.servicehub.entity.Servico;

@Component
public class ServicoMapper {

    public Servico toEntity(CriarServicoRequest request) {
        return new Servico(
                request.nome(),
                request.duracaoMinutos(),
                request.preco(),
                null
        );
    }

    public ServicoResponse toResponse(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getEmpresa().getId(),
                servico.getNome(),
                servico.getDuracaoMinutos(),
                servico.getPreco()
        );
    }
}
