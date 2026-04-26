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
import thiagosbarros.com.servicehub.entity.Empresa;
import thiagosbarros.com.servicehub.exception.EmpresaNaoEncontradaException;
import thiagosbarros.com.servicehub.mapper.EmpresaMapper;
import thiagosbarros.com.servicehub.service.EmpresaService;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmpresaController.class)
@Import({EmpresaMapper.class, GlobalExceptionHandler.class})
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmpresaService empresaService;

    @Test
    void deveCriarEmpresaERetornarCreated() throws Exception {
        // arrange
        Empresa empresa = new Empresa("ZILFARMA", null);
        ReflectionTestUtils.setField(empresa, "id", 1L);

        Mockito.when(empresaService.salvar(Mockito.any(Empresa.class))).thenReturn(empresa);

        String requestBody = """
                {
                  "nome": "ZILFARMA"
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/empresas/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ZILFARMA"));
    }

    @Test
    void deveBuscarEmpresaPorIdERetornarOk() throws Exception {
        // arrange
        Empresa empresa = new Empresa("ZILFARMA", null);
        ReflectionTestUtils.setField(empresa, "id", 1L);

        Mockito.when(empresaService.buscarPorId(1L)).thenReturn(empresa);

        // act + assert
        mockMvc.perform(get("/empresas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ZILFARMA"));
    }

    @Test
    void deveRetornarBadRequestQuandoRequestDeEmpresaForInvalido() throws Exception {
        // arrange
        String requestBody = """
                {
                  "nome": ""
                }
                """;

        // act + assert
        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));

        Mockito.verify(empresaService, Mockito.never()).salvar(Mockito.any(Empresa.class));
    }

    @Test
    void deveRetornarNotFoundQuandoEmpresaNaoExistir() throws Exception {
        // arrange
        Mockito.when(empresaService.buscarPorId(eq(1L)))
                .thenThrow(new EmpresaNaoEncontradaException("Empresa nao encontrada para o id: 1"));

        // act + assert
        mockMvc.perform(get("/empresas/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}
