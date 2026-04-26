package thiagosbarros.com.servicehub.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thiagosbarros.com.servicehub.controller.dto.CriarServicoRequest;
import thiagosbarros.com.servicehub.controller.dto.ServicoResponse;
import thiagosbarros.com.servicehub.entity.Servico;
import thiagosbarros.com.servicehub.mapper.ServicoMapper;
import thiagosbarros.com.servicehub.service.ServicoService;

import java.net.URI;

@RestController
@RequestMapping("/empresas/{empresaId}/servicos")
public class ServicoController {

    private final ServicoService servicoService;
    private final ServicoMapper servicoMapper;

    public ServicoController(ServicoService servicoService, ServicoMapper servicoMapper) {
        this.servicoService = servicoService;
        this.servicoMapper = servicoMapper;
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> criar(@PathVariable Long empresaId,
                                                 @Valid @RequestBody CriarServicoRequest request) {
        Servico servico = servicoService.salvar(empresaId, servicoMapper.toEntity(request));
        ServicoResponse response = servicoMapper.toResponse(servico);

        URI location = URI.create("/empresas/" + empresaId + "/servicos/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> buscarPorId(@PathVariable Long empresaId, @PathVariable Long id) {
        Servico servico = servicoService.buscarPorId(empresaId, id);
        return ResponseEntity.ok(servicoMapper.toResponse(servico));
    }
}
