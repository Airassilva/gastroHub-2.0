package pos.tech.cleanarchandjpa.ut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.dto.restaurante.HorarioFuncionamentoDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteUpdateDTO;
import pos.tech.cleanarchandjpa.core.exception.RestauranteNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.usecase.restaurante.*;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.RestauranteMapper;
import pos.tech.cleanarchandjpa.infra.http.controller.RestauranteController;
import pos.tech.cleanarchandjpa.infra.http.exceptions.GlobalExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestauranteControllerTest {
    @Mock
    CriarRestauranteUsecase criarRestauranteUsecase;

    @Mock
    ListarRestauranteUseCase listarRestauranteUsecase;

    @Mock
    AtualizarRestauranteComEnderecoUseCase atualizarRestauranteUsecase;

    @Mock
    DeletarRestauranteUseCase deletarRestauranteUsecase;

    RestauranteController restauranteController;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    AutoCloseable openMocks;

    MockedStatic<RestauranteMapper> restauranteMapperMock;
    MockedStatic<PaginacaoMapper> paginacaoMapperMock;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        restauranteController = new RestauranteController(
                criarRestauranteUsecase,
                listarRestauranteUsecase,
                deletarRestauranteUsecase,
                atualizarRestauranteUsecase
        );

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        PageableHandlerMethodArgumentResolver pageableResolver =
                new PageableHandlerMethodArgumentResolver();

        mockMvc = MockMvcBuilders
                .standaloneSetup(restauranteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(pageableResolver)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .setValidator(validator)
                .build();

        restauranteMapperMock = mockStatic(RestauranteMapper.class);
        paginacaoMapperMock = mockStatic(PaginacaoMapper.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (restauranteMapperMock != null) restauranteMapperMock.close();
        if (paginacaoMapperMock != null) paginacaoMapperMock.close();
        openMocks.close();
    }

    @Test
    void deveCriarRestaurante_Retornar201_ComBody() throws Exception {
        UUID usuarioIdDono = UUID.randomUUID();
        RestauranteRequestDTO request = criarRestauranteRequest();
        Restaurante restauranteDominio = criarRestauranteDominio(request);
        Restaurante restauranteCriado = criarRestauranteCriado(request);
        RestauranteResponseDTO responseDTO = criarRestauranteResponse();

        restauranteMapperMock.when(() -> RestauranteMapper.paraDominioDeRequest(request))
                .thenReturn(restauranteDominio);
        when(criarRestauranteUsecase.criarRestaurante(usuarioIdDono, restauranteDominio))
                .thenReturn(restauranteCriado);
        restauranteMapperMock.when(() -> RestauranteMapper.paraResponseDTO(restauranteCriado))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/restaurante/" + usuarioIdDono)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));

        verify(criarRestauranteUsecase).criarRestaurante(usuarioIdDono, restauranteDominio);
    }

    @Test
    void devePropagarUsuarioNaoEncontradoException_aoCriar() {
        UUID usuarioIdDono = UUID.randomUUID();
        var request = criarRestauranteRequest();
        var restauranteDominio = criarRestauranteDominio(request);

        restauranteMapperMock.when(() -> RestauranteMapper.paraDominioDeRequest(request))
                .thenReturn(restauranteDominio);
        when(criarRestauranteUsecase.criarRestaurante(usuarioIdDono, restauranteDominio))
                .thenThrow(new UsuarioNaoEncontradoException());

        assertThrows(UsuarioNaoEncontradoException.class,
                () -> restauranteController.criarRestaurante(usuarioIdDono, request));
        verify(criarRestauranteUsecase).criarRestaurante(usuarioIdDono, restauranteDominio);
    }

    @Test
    void deveListarRestaurantes_Retornar200_ComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);

        var parametros = new ParametrosPag(0, 10, null, null);
        paginacaoMapperMock.when(() -> PaginacaoMapper.dePageableParaParametrosPag(pageable))
                .thenReturn(parametros);

        @SuppressWarnings("unchecked")
        var paginaDominio = mock(PaginacaoResult.class);
        var paginaResponse = PaginacaoMapper.paraResponsePaginacaoRestaurante(paginaDominio);

        when(listarRestauranteUsecase.listarRestaurantes(parametros))
                .thenReturn(paginaDominio);

        paginacaoMapperMock.when(() -> PaginacaoMapper.paraResponsePaginacaoRestaurante(paginaDominio))
                .thenReturn(paginaResponse);

        ResponseEntity<PaginacaoResult<RestauranteResponseDTO>> response =
                restauranteController.listarRestaurantes(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(paginaResponse);
        verify(listarRestauranteUsecase).listarRestaurantes(parametros);
    }

    @Test
    void deveAtualizarRestaurante_Retornar200_ComBody() {
        UUID id = UUID.randomUUID();
        var updateDTO = criarRestauranteUpdate();
        var restauranteDominio = criarRestauranteDominio(updateDTO);
        var restauranteAtualizado = restauranteDominio;
        var responseDTO = criarRestauranteResponse();

        restauranteMapperMock.when(() -> RestauranteMapper.paraDominioDeDTO(updateDTO))
                .thenReturn(restauranteDominio);
        when(atualizarRestauranteUsecase.atualizarRestauranteComEndereco(restauranteDominio, id))
                .thenReturn(restauranteAtualizado);
        restauranteMapperMock.when(() -> RestauranteMapper.paraResponseDTO(restauranteAtualizado))
                .thenReturn(responseDTO);

        ResponseEntity<RestauranteResponseDTO> response =
                restauranteController.atualizarRestaurante(id, updateDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(responseDTO);
        verify(atualizarRestauranteUsecase)
                .atualizarRestauranteComEndereco(restauranteDominio, id);
    }

    @Test
    void devePropagarRestauranteNaoEncontradoException_aoAtualizar() {
        UUID id = UUID.randomUUID();
        var updateDTO = criarRestauranteUpdate();
        var restauranteDominio = criarRestauranteDominio(updateDTO);

        restauranteMapperMock.when(() -> RestauranteMapper.paraDominioDeDTO(updateDTO))
                .thenReturn(restauranteDominio);
        when(atualizarRestauranteUsecase.atualizarRestauranteComEndereco(restauranteDominio, id))
                .thenThrow(new RestauranteNaoEncontradoException());

        assertThrows(RestauranteNaoEncontradoException.class,
                () -> restauranteController.atualizarRestaurante(id, updateDTO));
        verify(atualizarRestauranteUsecase)
                .atualizarRestauranteComEndereco(restauranteDominio, id);
    }

    @Test
    void deveDeletarRestaurante_Retornar204() {
        UUID id = UUID.randomUUID();
        doNothing().when(deletarRestauranteUsecase).deletarRestaurante(id);

        ResponseEntity<Void> response = restauranteController.deletarRestaurante(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(deletarRestauranteUsecase).deletarRestaurante(id);
    }

    @Test
    void devePropagarRestauranteNaoEncontradoException_aoDeletar() {
        UUID id = UUID.randomUUID();
        doThrow(new RestauranteNaoEncontradoException())
                .when(deletarRestauranteUsecase).deletarRestaurante(id);

        assertThrows(RestauranteNaoEncontradoException.class,
                () -> restauranteController.deletarRestaurante(id));
        verify(deletarRestauranteUsecase).deletarRestaurante(id);
    }

     private RestauranteRequestDTO criarRestauranteRequest() {
         RestauranteRequestDTO request = new RestauranteRequestDTO();
         request.setNome("Restaurante Teste");
         request.setTipoDeCozinha("Italiana");
         request.setEndereco(criarEnderecoDTO());
         request.setHorarios(criarHorariosFuncionamento());
         return request;
     }

     private EnderecoDTO criarEnderecoDTO() {
         EnderecoDTO endereco = new EnderecoDTO();
         endereco.setRua("Rua Teste");
         endereco.setNumero("123");
         endereco.setBairro("Centro");
         endereco.setCidade("São Paulo");
         endereco.setEstado("SP");
         endereco.setCep("01234-567");
         return endereco;
     }

     private List<HorarioFuncionamentoDTO> criarHorariosFuncionamento() {
         List<HorarioFuncionamentoDTO> horarios = new ArrayList<>();

         // Segunda a Sexta
         horarios.add(criarHorario("SEGUNDA", "11:00", "23:00", false));
         horarios.add(criarHorario("TERCA", "11:00", "23:00", false));
         horarios.add(criarHorario("QUARTA", "11:00", "23:00", false));
         horarios.add(criarHorario("QUINTA", "11:00", "23:00", false));
         horarios.add(criarHorario("SEXTA", "11:00", "23:00", false));

         // Sábado e Domingo
         horarios.add(criarHorario("SABADO", "12:00", "00:00", false));
         horarios.add(criarHorario("DOMINGO", "00:00", "00:00", true)); // Fechado aos domingos

         return horarios;
     }

     private HorarioFuncionamentoDTO criarHorario(String dia, String abertura, String fechamento, boolean fechado) {
         HorarioFuncionamentoDTO horario = new HorarioFuncionamentoDTO();
         horario.setDiaSemana(dia);
         horario.setAbertura(abertura);
         horario.setFechamento(fechamento);
         horario.setFechado(fechado);
         return horario;
     }

     private RestauranteUpdateDTO criarRestauranteUpdate() {
        List<HorarioFuncionamentoDTO> listaHorarios = new ArrayList<>();
        RestauranteUpdateDTO updateDTO = new RestauranteUpdateDTO();
        updateDTO.setTipoDeCozinha("Japonesa");
        updateDTO.setHorarioFuncionamentoDTO(listaHorarios);
        return updateDTO;
    }

    private Restaurante criarRestauranteDominio(RestauranteRequestDTO request) {
        return RestauranteMapper.paraDominioDeRequest(request);
    }

    private Restaurante criarRestauranteDominio(RestauranteUpdateDTO updateDTO) {
        return RestauranteMapper.paraDominioDeDTO(updateDTO);
    }

    private RestauranteResponseDTO criarRestauranteResponse() {
        RestauranteResponseDTO responseDTO = new RestauranteResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setNomeRestaurante("Restaurante Teste");
        responseDTO.setIdUsuarioDono(UUID.randomUUID());
        return responseDTO;
    }

    private Restaurante criarRestauranteCriado(RestauranteRequestDTO requestDTO) {
        return criarRestauranteDominio(requestDTO);
    }
}
