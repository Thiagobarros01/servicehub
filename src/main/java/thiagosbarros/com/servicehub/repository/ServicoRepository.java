package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Servico findByEmpresaIdAndNome(Long empresaId, String nome);
}
