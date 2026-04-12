package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Agendamento findByEmpresaIdAndId(Long empresaId, Long id);
}
