package thiagosbarros.com.servicehub.mapper;

import org.springframework.stereotype.Component;
import thiagosbarros.com.servicehub.controller.dto.AgendamentoResponse;
import thiagosbarros.com.servicehub.entity.Agendamento;

@Component
public class AgendamentoMapper {

    public AgendamentoResponse toResponse(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getEmpresa().getId(),
                agendamento.getCliente().getId(),
                agendamento.getServico().getId(),
                agendamento.getDataHoraInicio(),
                agendamento.getStatus().name()
        );
    }
}
