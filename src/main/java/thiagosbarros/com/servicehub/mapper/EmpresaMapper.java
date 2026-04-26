package thiagosbarros.com.servicehub.mapper;

import org.springframework.stereotype.Component;
import thiagosbarros.com.servicehub.controller.dto.CriarEmpresaRequest;
import thiagosbarros.com.servicehub.controller.dto.EmpresaResponse;
import thiagosbarros.com.servicehub.entity.Empresa;

@Component
public class EmpresaMapper {

    public Empresa toEntity(CriarEmpresaRequest request) {
        return new Empresa(request.nome(), null);
    }

    public EmpresaResponse toResponse(Empresa empresa) {
        return new EmpresaResponse(
                empresa.getId(),
                empresa.getNome()
        );
    }
}
