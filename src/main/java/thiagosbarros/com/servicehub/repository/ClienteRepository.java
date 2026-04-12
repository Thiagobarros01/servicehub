package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Cliente;

import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmpresaIdAndEmail(Long empresaId, String email);

    Optional<Cliente> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Cliente> findByEmpresaId(Long empresaId);
}
