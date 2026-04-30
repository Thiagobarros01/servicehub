package thiagosbarros.com.servicehub.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import thiagosbarros.com.servicehub.controller.handler.GlobalExceptionHandler;
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.entity.Servico;
import thiagosbarros.com.servicehub.exception.BusinessException;
import thiagosbarros.com.servicehub.exception.ServicoNaoEncontradoException;
import thiagosbarros.com.servicehub.mapper.ServicoMapper;
import thiagosbarros.com.servicehub.service.ServicoService;

import java.math.BigDecimal;

@WebMvcTest(ServicoController.class)
@Import({ServicoMapper.class, GlobalExceptionHandler.class})
public class ServicoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ServicoService servicoService;


  @Test
  void deveCriarServicoERetornarCreated() throws Exception {
    //arrange
    Empresa empresa = new Empresa("ZILFARMA", null);
    ReflectionTestUtils.setField(empresa, "id", 1L);

    Servico servico = new Servico("TS TEC", 3600, BigDecimal.valueOf(100.0), empresa);
    ReflectionTestUtils.setField(servico, "id", 1L);

    Mockito.when(servicoService.salvar(ArgumentMatchers.eq(1L), Mockito.any(Servico.class))).thenReturn(servico);

    String requestBody = """
        {
        "nome":"TS TEC",
        "duracaoMinutos": 3600,
        "preco": 100.0
        }
        """;

    //act + assert

    mockMvc.perform(MockMvcRequestBuilders.post("/empresas/{empresaId}/servicos", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string("Location", "/empresas/1/servicos/1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("TS TEC"));

  }
  
  @Test
  void deveBuscarServicoPorIdERetornarOk() throws Exception {
    // arrange
    Empresa empresa = new Empresa("ZILFARMA", null);
    ReflectionTestUtils.setField(empresa, "id", 1L);

    Servico servico = new Servico("TS TEC", 3600, BigDecimal.valueOf(100.0), empresa);
    ReflectionTestUtils.setField(servico, "id", 1L);

    Mockito.when(servicoService.buscarPorId(1L, 1L)).thenReturn(servico);
    // act + assert
    mockMvc.perform(MockMvcRequestBuilders.get("/empresas/{empresaId}/servicos/{id}", 1L, 1L))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("TS TEC"));
  }

  @Test
  void deveRetornarBadRequestQuandoRequestDeServicoForInvalido() throws Exception {
    // arrange
    String requestBody = """
        {
        "nome":"",
        "duracaoMinutos": 0,
        "preco": -1.0
        }
        """;

    // act + assert
    mockMvc.perform(MockMvcRequestBuilders.post("/empresas/{empresaId}/servicos", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request"));

    Mockito.verify(servicoService, Mockito.never()).salvar(Mockito.anyLong(), Mockito.any(Servico.class));
  }

  @Test
  void deveRetornarUnprocessableEntityQuandoNomeDoServicoJaExistir() throws Exception {
    // arrange
    Mockito.when(servicoService.salvar(ArgumentMatchers.eq(1L), Mockito.any(Servico.class)))
        .thenThrow(new BusinessException("Ja existe um servico com este nome na empresa informada."));

    String requestBody = """
        {
        "nome":"TS TEC",
        "duracaoMinutos": 3600,
        "preco": 100.0
        }
        """;

    // act + assert
    mockMvc.perform(MockMvcRequestBuilders.post("/empresas/{empresaId}/servicos", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(422))
        .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Unprocessable Entity"));
  }

  @Test
  void deveRetornarNotFoundQuandoServicoNaoExistir() throws Exception {
    // arrange
    Mockito.when(servicoService.buscarPorId(1L, 1L))
        .thenThrow(new ServicoNaoEncontradoException("Servico nao encontrado para o id: 1"));

    // act + assert
    mockMvc.perform(MockMvcRequestBuilders.get("/empresas/{empresaId}/servicos/{id}", 1L, 1L))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
        .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found"));
  }

}
