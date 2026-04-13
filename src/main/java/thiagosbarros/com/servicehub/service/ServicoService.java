package thiagosbarros.com.servicehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Servico;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ServicoNaoEncontradoException;
import thiagosbarros.com.servicehub.repository.ServicoRepository;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final EmpresaService empresaService;

    public ServicoService(ServicoRepository servicoRepository, EmpresaService empresaService) {
        this.servicoRepository = servicoRepository;
        this.empresaService = empresaService;
    }

    @Transactional
    public Servico salvar(Long empresaId, Servico servico) {
        Empresa empresa = empresaService.buscarPorId(empresaId);

        if (servicoRepository.findByEmpresaIdAndNome(empresaId, servico.getNome()).isPresent()) {
            throw new BusinessException("Ja existe um servico com este nome na empresa informada.");
        }

        servico.setEmpresa(empresa);
        return servicoRepository.save(servico);
    }

    @Transactional(readOnly = true)
    public Servico buscarPorId(Long empresaId, Long id) {
        return servicoRepository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() -> new ServicoNaoEncontradoException("Servico nao encontrado para o id: " + id));
    }

    @Transactional(readOnly = true)
    public Servico buscarPorNomeDaEmpresa(Long empresaId, String nome) {
        return servicoRepository.findByEmpresaIdAndNome(empresaId, nome)
                .orElseThrow(() -> new ServicoNaoEncontradoException("Servico nao encontrado para a empresa e nome informados."));
    }
}
