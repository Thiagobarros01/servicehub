package thiagosbarros.com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thiagosbarros.com.servicehub.entity.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Pagamento findByAgendamentoIdAndAgendamentoEmpresaId(Long agendamentoId, Long empresaId);
}
