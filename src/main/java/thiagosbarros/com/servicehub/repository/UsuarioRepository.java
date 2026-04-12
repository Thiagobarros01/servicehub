package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmpresaIdAndEmail(Long empresaId, String email);
}
