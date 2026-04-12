package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
