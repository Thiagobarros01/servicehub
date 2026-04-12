package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmpresaIdAndEmail(Long empresaId, String email);
}
