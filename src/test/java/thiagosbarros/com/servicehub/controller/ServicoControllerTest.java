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
  void deveCriarServicoERetornarCreated() throws Exception{
    //arrange
    Empresa empresa = new Empresa("ZILFARMA", null);
    ReflectionTestUtils.setField(empresa, "id", 1L);

    Servico servico = new Servico("TS TEC", 3600, BigDecimal.valueOf(100.0), empresa);
    ReflectionTestUtils.setField(servico, "id", 1L);

    Mockito.when(servicoService.salvar(ArgumentMatchers.eq(1L),Mockito.any(Servico.class))).thenReturn(servico);

    String requestBody = """
            {
            "nome":"TS TEC",
            "duracaoMinutos": 3600,
            "preco": 100.0
            }
            """;

    //act + assert

    mockMvc.perform(MockMvcRequestBuilders.post("/empresas/{empresaId}/servicos",1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.header().string("Location", "/empresas/1/servicos/1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("TS TEC"));

  }

  

}
