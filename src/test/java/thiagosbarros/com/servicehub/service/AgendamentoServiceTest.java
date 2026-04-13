package thiagosbarros.com.servicehub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import thiagosbarros.com.servicehub.entity.Agendamento;
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Servico;
import thiagosbarros.com.servicehub.entity.Usuario;
import thiagosbarros.com.servicehub.entity.enums.Role;
import thiagosbarros.com.servicehub.entity.enums.StatusAgendamento;
import thiagosbarros.com.servicehub.repository.AgendamentoRepository;
import thiagosbarros.com.servicehub.repository.ClienteRepository;
import thiagosbarros.com.servicehub.repository.EmpresaRepository;
import thiagosbarros.com.servicehub.repository.ServicoRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ServicoRepository servicoRepository;
    @Mock
    private EmpresaRepository empresaRepository;
    @Mock
    private AgendamentoRepository agendamentoRepository;

    private Clock clock;

    private AgendamentoService agendamentoService;


    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2026-04-13T15:00:00Z"), ZoneOffset.UTC);

        agendamentoService = new AgendamentoService(
                clienteRepository,
                empresaRepository,
                agendamentoRepository,
                servicoRepository,
                clock
        );
    }

    @Test
    void deveCriarAgendamentoComSucesso() {
        // arrange
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant dataHoraInicio = Instant.parse("2026-04-13T16:00:00Z");

        Usuario dono = new Usuario("Thiago", "thiago@gmail.com", "123", Role.DONO, null);
        Empresa empresa = new Empresa("ZILFARMA", dono);
        Cliente cliente = new Cliente("Teta", "teta@gmail.com", LocalDate.of(1998, 1, 10), empresa);
        Servico servico = new Servico("Corte", 60, BigDecimal.valueOf(50), empresa);

        Mockito.when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresa));
        Mockito.when(clienteRepository.findByIdAndEmpresaId(clienteId,empresaId)).thenReturn(Optional.of(cliente));
        Mockito.when(servicoRepository.findByEmpresaIdAndId(empresaId,servicoId)).thenReturn(Optional.of(servico));
        Mockito.when(agendamentoRepository.findByEmpresaIdAndServicoIdAndDataHoraInicioBefore(empresaId,servicoId,dataHoraInicio.plusSeconds(3600)))
                .thenReturn(List.of());

        Mockito.when(agendamentoRepository.save(Mockito.any(Agendamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act

        Agendamento agendamento = agendamentoService.criar(empresaId,clienteId,servicoId,dataHoraInicio);

        // assert

        assertNotNull(agendamento);
        assertEquals(StatusAgendamento.PENDENTE, agendamento.getStatus());
        assertEquals(cliente, agendamento.getCliente());
        assertEquals(servico, agendamento.getServico());
        assertEquals(empresa, agendamento.getEmpresa());
        assertEquals(dataHoraInicio, agendamento.getDataHoraInicio());

    }



}