package thiagosbarros.com.servicehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ClienteNaoEncontradoException;
import thiagosbarros.com.servicehub.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EmpresaService empresaService;

    public ClienteService(ClienteRepository clienteRepository, EmpresaService empresaService) {
        this.clienteRepository = clienteRepository;
        this.empresaService = empresaService;
    }

    @Transactional
    public Cliente salvar(Long empresaId, Cliente cliente) {
        Empresa empresa = empresaService.buscarPorId(empresaId);

        if (clienteRepository.findByEmpresaIdAndEmail(empresaId, cliente.getEmail()).isPresent()) {
            throw new BusinessException("Ja existe um cliente com este email na empresa informada.");
        }

        cliente.setEmpresa(empresa);
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long empresaId, Long id) {
        return clienteRepository.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente nao encontrado para o id: " + id));
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorEmailDaEmpresa(Long empresaId, String email) {
        return clienteRepository.findByEmpresaIdAndEmail(empresaId, email)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente nao encontrado para a empresa e email informados."));
    }
}
