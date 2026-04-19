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
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ClienteNaoEncontradoException;
import thiagosbarros.com.servicehub.exception.EmpresaNaoEncontradaException;
import thiagosbarros.com.servicehub.exception.ServicoNaoEncontradoException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private Empresa empresa;
    private Cliente cliente;
    private Servico servico;

    @BeforeEach
    void setUp() {
        Usuario dono = new Usuario("Thiago", "thiago@gmail.com", "123", Role.DONO, null);
        empresa = new Empresa("ZILFARMA", dono);
        cliente = new Cliente("Teta", "teta@gmail.com", LocalDate.of(1998, 1, 10), empresa);
        servico = new Servico("Corte", 60, BigDecimal.valueOf(50), empresa);

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

        Mockito.when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresa));
        Mockito.when(clienteRepository.findByIdAndEmpresaId(clienteId, empresaId)).thenReturn(Optional.of(cliente));
        Mockito.when(servicoRepository.findByEmpresaIdAndId(empresaId, servicoId)).thenReturn(Optional.of(servico));
        Mockito.when(agendamentoRepository.findByEmpresaIdAndServicoIdAndDataHoraInicioBefore(
                empresaId, servicoId, dataHoraInicio.plusSeconds(3600)
        )).thenReturn(List.of());
        Mockito.when(agendamentoRepository.save(Mockito.any(Agendamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Agendamento agendamento = agendamentoService.criar(empresaId, clienteId, servicoId, dataHoraInicio);

        // assert
        assertNotNull(agendamento);
        assertEquals(StatusAgendamento.PENDENTE, agendamento.getStatus());
        assertEquals(cliente, agendamento.getCliente());
        assertEquals(servico, agendamento.getServico());
        assertEquals(empresa, agendamento.getEmpresa());
        assertEquals(dataHoraInicio, agendamento.getDataHoraInicio());
    }

    @Test
    void deveLancarExcecaoQuandoHouverConflitoDeHorario() {
        // arrange
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant novoInicio = Instant.parse("2026-04-13T16:00:00Z");

        Agendamento existente = new Agendamento(
                Instant.parse("2026-04-13T15:30:00Z"),
                cliente,
                servico,
                empresa
        );

        Mockito.when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresa));
        Mockito.when(clienteRepository.findByIdAndEmpresaId(clienteId, empresaId)).thenReturn(Optional.of(cliente));
        Mockito.when(servicoRepository.findByEmpresaIdAndId(empresaId, servicoId)).thenReturn(Optional.of(servico));
        Mockito.when(agendamentoRepository.findByEmpresaIdAndServicoIdAndDataHoraInicioBefore(
                empresaId, servicoId, novoInicio.plusSeconds(3600)
        )).thenReturn(List.of(existente));

        // act + assert
        assertThrows(BusinessException.class, () ->
                agendamentoService.criar(empresaId, clienteId, servicoId, novoInicio)
        );

        // assert
        Mockito.verify(agendamentoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoEmpresaNaoExistir() {
        // arrange
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant dataHoraInicio = Instant.parse("2026-04-13T16:00:00Z");

        Mockito.when(empresaRepository.findById(empresaId)).thenReturn(Optional.empty());

        // act + assert
        assertThrows(EmpresaNaoEncontradaException.class, () ->
                agendamentoService.criar(empresaId, clienteId, servicoId, dataHoraInicio)
        );

        // assert
        Mockito.verify(clienteRepository, Mockito.never()).findByIdAndEmpresaId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(servicoRepository, Mockito.never()).findByEmpresaIdAndId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(agendamentoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoDataForNoPassado() {
        // arrange
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant dataHoraInicio = Instant.parse("2026-04-13T14:59:00Z");

        // act + assert
        assertThrows(BusinessException.class, () ->
                agendamentoService.criar(empresaId, clienteId, servicoId, dataHoraInicio)
        );

        // assert
        Mockito.verify(empresaRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(clienteRepository, Mockito.never()).findByIdAndEmpresaId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(servicoRepository, Mockito.never()).findByEmpresaIdAndId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(agendamentoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        // arrange - cenário
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant dataHoraInicio = Instant.parse("2026-04-13T16:00:00Z");

        Mockito.when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresa));
        Mockito.when(clienteRepository.findByIdAndEmpresaId(clienteId, empresaId)).thenReturn(Optional.empty());

        // act + assert - ação
        assertThrows(ClienteNaoEncontradoException.class, () ->
                agendamentoService.criar(empresaId, clienteId, servicoId, dataHoraInicio)
        );

        // assert
        Mockito.verify(empresaRepository).findById(empresaId);
        Mockito.verify(clienteRepository).findByIdAndEmpresaId(clienteId, empresaId);
        Mockito.verify(servicoRepository, Mockito.never()).findByEmpresaIdAndId(Mockito.anyLong(), Mockito.anyLong());
        Mockito.verify(agendamentoRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoServicoNaoExistir(){
        //Cenário

        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;

        Instant dataHoraInicio = Instant.parse("2026-04-15T17:00:00Z");

        Mockito.when(empresaRepository.findById(empresaId)).thenReturn(Optional.of(empresa));
        Mockito.when(clienteRepository.findByIdAndEmpresaId(clienteId, empresaId)).thenReturn(Optional.of(cliente));
        Mockito.when(servicoRepository.findByEmpresaIdAndId(empresaId, servicoId))
                .thenReturn(Optional.empty());


        // Ação
        assertThrows(ServicoNaoEncontradoException.class, ()->
       agendamentoService.criar(empresaId, clienteId, servicoId, dataHoraInicio));


        //Assert

       Mockito.verify(empresaRepository).findById(empresaId);
       Mockito.verify(clienteRepository).findByIdAndEmpresaId(clienteId, empresaId);
       Mockito.verify(servicoRepository).findByEmpresaIdAndId(empresaId, servicoId);
       Mockito.verify(agendamentoRepository, Mockito.never()).save(Mockito.any());

    }

   
}
