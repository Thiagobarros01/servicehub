package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmpresaIdAndEmail(Long empresaId, String email);

    Optional<Usuario> findByEmpresaIdAndId(Long empresaId, Long id);
}
