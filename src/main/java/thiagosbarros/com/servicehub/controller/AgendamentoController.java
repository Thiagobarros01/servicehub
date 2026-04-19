package thiagosbarros.com.servicehub.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thiagosbarros.com.servicehub.controller.dto.AgendamentoResponse;
import thiagosbarros.com.servicehub.controller.dto.CriarAgendamentoRequest;
import thiagosbarros.com.servicehub.entity.Agendamento;
import thiagosbarros.com.servicehub.mapper.AgendamentoMapper;
import thiagosbarros.com.servicehub.service.AgendamentoService;

import java.net.URI;

@RestController
@RequestMapping("/empresas/{empresaId}/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final AgendamentoMapper agendamentoMapper;

    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoMapper agendamentoMapper) {
        this.agendamentoService = agendamentoService;
        this.agendamentoMapper = agendamentoMapper;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> criar(@PathVariable Long empresaId,
                                                     @Valid @RequestBody CriarAgendamentoRequest request) {
        Agendamento agendamento = agendamentoService.criar(
                empresaId,
                request.clienteId(),
                request.servicoId(),
                request.dataHoraInicio()
        );
        AgendamentoResponse response = agendamentoMapper.toResponse(agendamento);

        URI location = URI.create("/empresas/" + empresaId + "/agendamentos/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }
}
