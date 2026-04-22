package thiagosbarros.com.servicehub.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import thiagosbarros.com.servicehub.controller.handler.GlobalExceptionHandler;
import thiagosbarros.com.servicehub.entity.Agendamento;
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Servico;
import thiagosbarros.com.servicehub.entity.Usuario;
import thiagosbarros.com.servicehub.entity.enums.Role;
import thiagosbarros.com.servicehub.entity.enums.StatusAgendamento;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ClienteNaoEncontradoException;
import thiagosbarros.com.servicehub.mapper.AgendamentoMapper;
import thiagosbarros.com.servicehub.service.AgendamentoService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgendamentoController.class)
@Import({AgendamentoMapper.class, GlobalExceptionHandler.class})
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgendamentoService agendamentoService;

    @Test
    void deveCriarAgendamentoERetornarCreated() throws Exception {
        // arrange
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant dataHoraInicio = Instant.parse("2099-04-20T16:00:00Z");

        Agendamento agendamento = criarAgendamentoSalvo(10L, empresaId, clienteId, servicoId, dataHoraInicio);

        Mockito.when(agendamentoService.criar(eq(empresaId), eq(clienteId), eq(servicoId), eq(dataHoraInicio)))
                .thenReturn(agendamento);

        String requestBody = """
                {
                  "clienteId": 2,
                  "servicoId": 3,
                  "dataHoraInicio": "2099-04-20T16:00:00Z"
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas/{empresaId}/agendamentos", empresaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/empresas/1/agendamentos/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.empresaId").value(1))
                .andExpect(jsonPath("$.clienteId").value(2))
                .andExpect(jsonPath("$.servicoId").value(3))
                .andExpect(jsonPath("$.dataHoraInicio").value("2099-04-20T16:00:00Z"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void deveRetornarBadRequestQuandoRequestForInvalido() throws Exception {
        // arrange
        String requestBody = """
                {
                  "servicoId": 3,
                  "dataHoraInicio": "2099-04-20T16:00:00Z"
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas/{empresaId}/agendamentos", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Request invalido."))
                .andExpect(jsonPath("$.fields[0].field").value("clienteId"));

        Mockito.verify(agendamentoService, Mockito.never())
                .criar(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.any());
    }

    @Test
    void deveRetornarUnprocessableEntityQuandoRegraDeNegocioFalhar() throws Exception {
        // arrange
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant dataHoraInicio = Instant.parse("2099-04-20T16:00:00Z");

        Mockito.when(agendamentoService.criar(eq(empresaId), eq(clienteId), eq(servicoId), eq(dataHoraInicio)))
                .thenThrow(new BusinessException("Ja existe agendamento no horario escolhido para este servico."));

        String requestBody = """
                {
                  "clienteId": 2,
                  "servicoId": 3,
                  "dataHoraInicio": "2099-04-20T16:00:00Z"
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas/{empresaId}/agendamentos", empresaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Unprocessable Entity"))
                .andExpect(jsonPath("$.message").value("Ja existe agendamento no horario escolhido para este servico."));
    }

    @Test
    void deveRetornarNotFoundQuandoClienteNaoExistir() throws Exception {
        // arrange
        Long empresaId = 1L;
        Long clienteId = 2L;
        Long servicoId = 3L;
        Instant dataHoraInicio = Instant.parse("2099-04-20T16:00:00Z");

        Mockito.when(agendamentoService.criar(eq(empresaId), eq(clienteId), eq(servicoId), eq(dataHoraInicio)))
                .thenThrow(new ClienteNaoEncontradoException("Cliente nao encontrado."));

        String requestBody = """
                {
                  "clienteId": 2,
                  "servicoId": 3,
                  "dataHoraInicio": "2099-04-20T16:00:00Z"
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas/{empresaId}/agendamentos", empresaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Cliente nao encontrado."));
    }

    private Agendamento criarAgendamentoSalvo(Long agendamentoId,
                                              Long empresaId,
                                              Long clienteId,
                                              Long servicoId,
                                              Instant dataHoraInicio) {
        Usuario dono = new Usuario("Thiago", "thiago@gmail.com", "123", Role.DONO, null);
        Empresa empresa = new Empresa("ZILFARMA", dono);
        Cliente cliente = new Cliente("Teta", "teta@gmail.com", LocalDate.of(1998, 1, 10), empresa);
        Servico servico = new Servico("Corte", 60, BigDecimal.valueOf(50), empresa);
        Agendamento agendamento = new Agendamento(dataHoraInicio, cliente, servico, empresa);
        agendamento.setStatus(StatusAgendamento.PENDENTE);

        ReflectionTestUtils.setField(empresa, "id", empresaId);
        ReflectionTestUtils.setField(cliente, "id", clienteId);
        ReflectionTestUtils.setField(servico, "id", servicoId);
        ReflectionTestUtils.setField(agendamento, "id", agendamentoId);

        return agendamento;
    }
}
