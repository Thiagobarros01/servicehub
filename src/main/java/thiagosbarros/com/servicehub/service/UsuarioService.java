package thiagosbarros.com.servicehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Usuario;
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

        Usuario usuarioExistente = usuarioRepository.findByEmpresaIdAndEmail(empresaId, usuario.getEmail());
        if (usuarioExistente != null) {
            throw new IllegalArgumentException("Ja existe um usuario com este email na empresa informada.");
        }

        usuario.setEmpresa(empresa);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado para o id: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmailDaEmpresa(Long empresaId, String email) {
        Usuario usuario = usuarioRepository.findByEmpresaIdAndEmail(empresaId, email);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario nao encontrado para a empresa e email informados.");
        }
        return usuario;
    }
}
