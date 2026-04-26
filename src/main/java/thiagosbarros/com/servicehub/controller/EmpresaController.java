package thiagosbarros.com.servicehub.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thiagosbarros.com.servicehub.controller.dto.CriarEmpresaRequest;
import thiagosbarros.com.servicehub.controller.dto.EmpresaResponse;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.mapper.EmpresaMapper;
import thiagosbarros.com.servicehub.service.EmpresaService;

import java.net.URI;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final EmpresaMapper empresaMapper;

    public EmpresaController(EmpresaService empresaService, EmpresaMapper empresaMapper) {
        this.empresaService = empresaService;
        this.empresaMapper = empresaMapper;
    }

    @PostMapping
    public ResponseEntity<EmpresaResponse> criar(@Valid @RequestBody CriarEmpresaRequest request) {
        Empresa empresa = empresaService.salvar(empresaMapper.toEntity(request));
        EmpresaResponse response = empresaMapper.toResponse(empresa);

        return ResponseEntity.created(URI.create("/empresas/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponse> buscarPorId(@PathVariable Long id) {
        Empresa empresa = empresaService.buscarPorId(id);
        return ResponseEntity.ok(empresaMapper.toResponse(empresa));
    }
}
