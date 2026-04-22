package thiagosbarros.com.servicehub.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import thiagosbarros.com.servicehub.entity.Agendamento;
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Servico;
import thiagosbarros.com.servicehub.entity.enums.StatusAgendamento;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.repository.AgendamentoRepository;
import thiagosbarros.com.servicehub.repository.ClienteRepository;
import thiagosbarros.com.servicehub.repository.EmpresaRepository;
import thiagosbarros.com.servicehub.repository.ServicoRepository;
import thiagosbarros.com.servicehub.service.AgendamentoService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class AgendamentoIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Test
    @Transactional
    void deveCriarAgendamentoComBancoReal() {
        // arrange
        DadosBase dados = criarDadosBase();
        Instant dataHoraInicio = Instant.parse("2099-04-21T16:00:00Z");

        // act
        Agendamento agendamento = agendamentoService.criar(
                dados.empresa().getId(),
                dados.cliente().getId(),
                dados.servico().getId(),
                dataHoraInicio
        );

        // assert
        assertNotNull(agendamento.getId());
        assertEquals(StatusAgendamento.PENDENTE, agendamento.getStatus());
        assertEquals(dataHoraInicio, agendamento.getDataHoraInicio());
        assertEquals(1, agendamentoRepository.count());
    }

    @Test
    @Transactional
    void deveBloquearAgendamentoConflitanteComBancoReal() {
        // arrange
        DadosBase dados = criarDadosBase();

        agendamentoService.criar(
                dados.empresa().getId(),
                dados.cliente().getId(),
                dados.servico().getId(),
                Instant.parse("2099-04-21T15:30:00Z")
        );

        // act + assert
        assertThrows(BusinessException.class, () ->
                agendamentoService.criar(
                        dados.empresa().getId(),
                        dados.cliente().getId(),
                        dados.servico().getId(),
                        Instant.parse("2099-04-21T16:00:00Z")
                )
        );

        assertEquals(1, agendamentoRepository.count());
    }

    private DadosBase criarDadosBase() {
        Empresa empresa = empresaRepository.save(new Empresa("ZILFARMA", null));
        Cliente cliente = clienteRepository.save(new Cliente("Teta", "teta@gmail.com", LocalDate.of(1998, 1, 10), empresa));
        Servico servico = servicoRepository.save(new Servico("Corte", 60, BigDecimal.valueOf(50), empresa));

        return new DadosBase(empresa, cliente, servico);
    }

    private record DadosBase(Empresa empresa, Cliente cliente, Servico servico) {
    }
}
