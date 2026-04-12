package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Agendamento;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findByEmpresaIdAndId(Long empresaId, Long id);

    List<Agendamento> findByEmpresaIdAndServicoIdAndDataHoraInicioBefore(Long empresaId,
                                                                         Long servicoId,
                                                                         Instant limiteSuperior);
}
