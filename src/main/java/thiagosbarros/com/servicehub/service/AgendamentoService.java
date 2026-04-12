package thiagosbarros.com.servicehub.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thiagosbarros.com.servicehub.entity.Agendamento;
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Servico;
import thiagosbarros.com.servicehub.entity.enums.StatusAgendamento;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ClienteNaoEncontradoException;
import thiagosbarros.com.servicehub.exception.EmpresaNaoEncontradaException;
import thiagosbarros.com.servicehub.exception.ServicoNaoEncontradoException;
import thiagosbarros.com.servicehub.repository.AgendamentoRepository;
import thiagosbarros.com.servicehub.repository.ClienteRepository;
import thiagosbarros.com.servicehub.repository.EmpresaRepository;
import thiagosbarros.com.servicehub.repository.ServicoRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class AgendamentoService {
    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final Clock clock;

    public AgendamentoService(ClienteRepository clienteRepository,
                              EmpresaRepository empresaRepository,
                              AgendamentoRepository agendamentoRepository,
                              ServicoRepository servicoRepository,
                              Clock clock) {
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.servicoRepository = servicoRepository;
        this.clock = clock;
    }

    @Transactional
    public Agendamento criar(Long empresaId, Long clienteId, Long servicoId, Instant dataHoraInicio) {
        Instant agora = Instant.now(clock);

        if (dataHoraInicio.isBefore(agora)) {
            throw new BusinessException("Nao e permitido criar agendamento no passado.");
        }

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EmpresaNaoEncontradaException("Empresa nao existe."));

        Cliente cliente = clienteRepository.findByIdAndEmpresaId(clienteId, empresaId)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente nao encontrado."));

        Servico servico = servicoRepository.findByEmpresaIdAndId(empresaId, servicoId)
                .orElseThrow(() -> new ServicoNaoEncontradoException("Servico nao encontrado."));

        Instant dataHoraFim = calcularFim(dataHoraInicio, servico);

        List<Agendamento> agendamentosDoServico = agendamentoRepository
                .findByEmpresaIdAndServicoIdAndDataHoraInicioBefore(empresaId, servicoId, dataHoraFim);

        boolean existeConflito = agendamentosDoServico.stream()
                .anyMatch(agendamentoExistente -> temConflito(agendamentoExistente, dataHoraInicio, dataHoraFim));

        if (existeConflito) {
            throw new BusinessException("Ja existe agendamento no horario escolhido para este servico.");
        }

        Agendamento agendamento = new Agendamento(dataHoraInicio, cliente, servico, empresa);
        agendamento.setStatus(StatusAgendamento.PENDENTE);

        return agendamentoRepository.save(agendamento);
    }

    private Instant calcularFim(Instant dataHoraInicio, Servico servico) {
        return dataHoraInicio.plusSeconds(servico.getDuracaoMinutos() * 60L);
    }

    private boolean temConflito(Agendamento agendamentoExistente, Instant novoInicio, Instant novoFim) {
        Instant inicioExistente = agendamentoExistente.getDataHoraInicio();
        Instant fimExistente = calcularFim(inicioExistente, agendamentoExistente.getServico());

        return novoInicio.isBefore(fimExistente) && novoFim.isAfter(inicioExistente);
    }
}
