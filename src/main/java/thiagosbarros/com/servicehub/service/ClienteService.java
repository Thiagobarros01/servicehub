package thiagosbarros.com.servicehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.entity.Empresa;
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

        Cliente clienteExistente = clienteRepository.findByEmpresaIdAndEmail(empresaId, cliente.getEmail());
        if (clienteExistente != null) {
            throw new ClienteNaoEncontradoException("Ja existe um cliente com este email na empresa informada.");
        }

        cliente.setEmpresa(empresa);
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente nao encontrado para o id: " + id));
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorEmailDaEmpresa(Long empresaId, String email) {
        Cliente cliente = clienteRepository.findByEmpresaIdAndEmail(empresaId, email);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente nao encontrado para a empresa e email informados.");
        }
        return cliente;
    }
}
