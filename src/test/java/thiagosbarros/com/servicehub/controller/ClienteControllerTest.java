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
import thiagosbarros.com.servicehub.entity.Cliente;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ClienteNaoEncontradoException;
import thiagosbarros.com.servicehub.mapper.ClienteMapper;
import thiagosbarros.com.servicehub.service.ClienteService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
@Import({ClienteMapper.class, GlobalExceptionHandler.class})
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void deveCriarClienteERetornarCreated() throws Exception {
        // arrange
        Empresa empresa = new Empresa("ZILFARMA", null);
        ReflectionTestUtils.setField(empresa, "id", 1L);

        Cliente cliente = new Cliente("Thiago", "thiago@gmail.com", LocalDate.of(1998, 1, 10), empresa);
        ReflectionTestUtils.setField(cliente, "id", 2L);

        Mockito.when(clienteService.salvar(eq(1L), Mockito.any(Cliente.class))).thenReturn(cliente);

        String requestBody = """
                {
                  "nome": "Thiago",
                  "email": "thiago@gmail.com",
                  "dataNascimento": "1998-01-10"
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas/{empresaId}/clientes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/empresas/1/clientes/2"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.empresaId").value(1))
                .andExpect(jsonPath("$.nome").value("Thiago"))
                .andExpect(jsonPath("$.email").value("thiago@gmail.com"));
    }

    @Test
    void deveBuscarClientePorIdERetornarOk() throws Exception {
        // arrange
        Empresa empresa = new Empresa("ZILFARMA", null);
        ReflectionTestUtils.setField(empresa, "id", 1L);

        Cliente cliente = new Cliente("Thiago", "thiago@gmail.com", LocalDate.of(1998, 1, 10), empresa);
        ReflectionTestUtils.setField(cliente, "id", 2L);

        Mockito.when(clienteService.buscarPorId(1L, 2L)).thenReturn(cliente);

        // act + assert
        mockMvc.perform(get("/empresas/{empresaId}/clientes/{id}", 1L, 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.empresaId").value(1))
                .andExpect(jsonPath("$.nome").value("Thiago"))
                .andExpect(jsonPath("$.email").value("thiago@gmail.com"));
    }

    @Test
    void deveRetornarUnprocessableEntityQuandoEmailDoClienteJaExistir() throws Exception {
        // arrange
        Mockito.when(clienteService.salvar(eq(1L), Mockito.any(Cliente.class)))
                .thenThrow(new BusinessException("Ja existe um cliente com este email na empresa informada."));

        String requestBody = """
                {
                  "nome": "Thiago",
                  "email": "thiago@gmail.com",
                  "dataNascimento": "1998-01-10"
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas/{empresaId}/clientes", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Unprocessable Entity"));
    }

    @Test
    void deveRetornarNotFoundQuandoClienteNaoExistir() throws Exception {
        // arrange
        Mockito.when(clienteService.buscarPorId(eq(1L), eq(2L)))
                .thenThrow(new ClienteNaoEncontradoException("Cliente nao encontrado para o id: 2"));

        // act + assert
        mockMvc.perform(get("/empresas/{empresaId}/clientes/{id}", 1L, 2L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
