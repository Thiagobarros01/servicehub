package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Servico;

import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findByEmpresaIdAndNome(Long empresaId, String nome);

    Optional<Servico> findByEmpresaIdAndId(Long empresaId, Long id);
}
