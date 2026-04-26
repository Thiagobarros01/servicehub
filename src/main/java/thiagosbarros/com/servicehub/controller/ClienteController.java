package thiagosbarros.com.servicehub.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thiagosbarros.com.servicehub.controller.dto.ClienteResponse;
import thiagosbarros.com.servicehub.controller.dto.CriarClienteRequest;
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.mapper.ClienteMapper;
import thiagosbarros.com.servicehub.service.ClienteService;

import java.net.URI;

@RestController
@RequestMapping("/empresas/{empresaId}/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@PathVariable Long empresaId,
                                                 @Valid @RequestBody CriarClienteRequest request) {
        Cliente cliente = clienteService.salvar(empresaId, clienteMapper.toEntity(request));
        ClienteResponse response = clienteMapper.toResponse(cliente);

        URI location = URI.create("/empresas/" + empresaId + "/clientes/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long empresaId, @PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(empresaId, id);
        return ResponseEntity.ok(clienteMapper.toResponse(cliente));
    }
}
