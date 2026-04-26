package thiagosbarros.com.servicehub.mapper;

import org.springframework.stereotype.Component;
import thiagosbarros.com.servicehub.controller.dto.ClienteResponse;
import thiagosbarros.com.servicehub.controller.dto.CriarClienteRequest;
import thiagosbarros.com.servicehub.entity.Cliente;

@Component
public class ClienteMapper {

    public Cliente toEntity(CriarClienteRequest request) {
        return new Cliente(
                request.nome(),
                request.email(),
                request.dataNascimento(),
                null
        );
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getEmpresa().getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataNascimento()
        );
    }
}
