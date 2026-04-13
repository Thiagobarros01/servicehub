package thiagosbarros.com.servicehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Usuario;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.UsuarioNaoEncontradoException;
import thiagosbarros.com.servicehub.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaService empresaService;

    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaService empresaService) {
        this.usuarioRepository = usuarioRepository;
        this.empresaService = empresaService;
    }

    @Transactional
    public Usuario salvar(Long empresaId, Usuario usuario) {
        Empresa empresa = empresaService.buscarPorId(empresaId);

        if (usuarioRepository.findByEmpresaIdAndEmail(empresaId, usuario.getEmail()).isPresent()) {
            throw new BusinessException("Ja existe um usuario com este email na empresa informada.");
        }

        usuario.setEmpresa(empresa);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long empresaId, Long id) {
        return usuarioRepository.findByEmpresaIdAndId(empresaId, id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao encontrado para o id: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmailDaEmpresa(Long empresaId, String email) {
        return usuarioRepository.findByEmpresaIdAndEmail(empresaId, email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario nao encontrado para a empresa e email informados."));
    }
}
