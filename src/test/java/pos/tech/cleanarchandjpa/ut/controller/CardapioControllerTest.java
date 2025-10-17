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
import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioUpdateDTO;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.exception.CardapioNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.exception.RestauranteNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.AtualizarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.CriarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.DeletarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.ListarCardapioPeloRestauranteUseCase;
import pos.tech.cleanarchandjpa.infra.database.mapper.CardapioMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.http.controller.CardapioController;
import pos.tech.cleanarchandjpa.infra.http.exceptions.GlobalExceptionHandler;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardapioControllerTest {

    @Mock
    CriarCardapioUseCase criarCardapioUseCase;

    @Mock
    AtualizarCardapioUseCase atualizarCardapioUseCase;

    @Mock
    ListarCardapioPeloRestauranteUseCase listarCardapioPeloRestauranteUseCase;

    @Mock
    DeletarCardapioUseCase deletarCardapioUseCase;

    CardapioController controller;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    AutoCloseable openMocks;

    MockedStatic<CardapioMapper> cardapioMapperMock;
    MockedStatic<PaginacaoMapper> paginacaoMapperMock;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        controller = new CardapioController(
                criarCardapioUseCase,
                atualizarCardapioUseCase,
                listarCardapioPeloRestauranteUseCase,
                deletarCardapioUseCase
        );

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        PageableHandlerMethodArgumentResolver pageableResolver =
                new PageableHandlerMethodArgumentResolver();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(pageableResolver)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .setValidator(validator)
                .build();

        cardapioMapperMock = mockStatic(CardapioMapper.class);
        paginacaoMapperMock = mockStatic(PaginacaoMapper.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (cardapioMapperMock != null) cardapioMapperMock.close();
        if (paginacaoMapperMock != null) paginacaoMapperMock.close();
        openMocks.close();
    }

    @Test
    void deveCriarCardapio_Retornar201_ComBody() throws Exception {
        UUID restauranteId = UUID.randomUUID();
        CardapioRequestDTO request = criarCardapioRequest();
        Cardapio cardapioDominio = criarCardapioDominio(request);
        Cardapio cardapioCriado = criarCardapioCriado(request);
        CardapioResponseDTO responseDTO = criarCardapioResponse();

        cardapioMapperMock.when(() -> CardapioMapper.paraDominioDeDto(request))
                .thenReturn(cardapioDominio);
        when(criarCardapioUseCase.criarCardapio(cardapioDominio, restauranteId))
                .thenReturn(cardapioCriado);
        cardapioMapperMock.when(() -> CardapioMapper.paraResponseDeDominio(cardapioCriado))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/cardapio/" + restauranteId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));

        verify(criarCardapioUseCase).criarCardapio(cardapioDominio, restauranteId);
    }

    @Test
    void devePropagarRestauranteNaoEncontradoException_aoCriar() {
        UUID restauranteId = UUID.randomUUID();
        var request = criarCardapioRequest();
        var cardapioDominio = criarCardapioDominio(request);

        cardapioMapperMock.when(() -> CardapioMapper.paraDominioDeDto(request))
                .thenReturn(cardapioDominio);
        when(criarCardapioUseCase.criarCardapio(cardapioDominio, restauranteId))
                .thenThrow(new RestauranteNaoEncontradoException());

        assertThrows(RestauranteNaoEncontradoException.class,
                () -> controller.criarCardapio(restauranteId, request));
        verify(criarCardapioUseCase).criarCardapio(cardapioDominio, restauranteId);
    }

    @Test
    void deveListarCardapioPorRestaurante_Retornar200_ComPaginacao() {
        UUID restauranteId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        var parametros = new ParametrosPag(0, 10, null, null);
        paginacaoMapperMock.when(() -> PaginacaoMapper.dePageableParaParametrosPag(pageable))
                .thenReturn(parametros);

        @SuppressWarnings("unchecked")
        var paginaDominio = mock(PaginacaoResult.class);
        var paginaResponse = mock(PaginacaoResult.class);

        when(listarCardapioPeloRestauranteUseCase.listarCardapioPorRestaurante(restauranteId, parametros))
                .thenReturn(paginaDominio);

        paginacaoMapperMock.when(() -> PaginacaoMapper.paraResponsePaginacaoCardapio(paginaDominio))
                .thenReturn(paginaResponse);

        ResponseEntity<PaginacaoResult<CardapioResponseDTO>> response =
                controller.listarCardapio(restauranteId, pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(listarCardapioPeloRestauranteUseCase).listarCardapioPorRestaurante(restauranteId, parametros);
    }

    @Test
    void devePropagarRestauranteNaoEncontradoException_aoListar() {
        UUID restauranteId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        var parametros = new ParametrosPag(0, 10, null, null);
        paginacaoMapperMock.when(() -> PaginacaoMapper.dePageableParaParametrosPag(pageable))
                .thenReturn(parametros);

        when(listarCardapioPeloRestauranteUseCase.listarCardapioPorRestaurante(restauranteId, parametros))
                .thenThrow(new RestauranteNaoEncontradoException());

        assertThrows(RestauranteNaoEncontradoException.class,
                () -> controller.listarCardapio(restauranteId, pageable));
        verify(listarCardapioPeloRestauranteUseCase).listarCardapioPorRestaurante(restauranteId, parametros);
    }

    @Test
    void deveAtualizarCardapio_Retornar200_ComBody() {
        UUID id = UUID.randomUUID();
        var updateDTO = criarCardapioUpdate();
        var cardapioDominio = criarCardapioDominio(updateDTO);
        var cardapioAtualizado = cardapioDominio;
        var responseDTO = criarCardapioResponse();

        cardapioMapperMock.when(() -> CardapioMapper.paraDominioDeDtoUpdate(updateDTO))
                .thenReturn(cardapioDominio);
        when(atualizarCardapioUseCase.atualizarCardapio(cardapioDominio, id))
                .thenReturn(cardapioAtualizado);
        cardapioMapperMock.when(() -> CardapioMapper.paraResponseDeDominio(cardapioAtualizado))
                .thenReturn(responseDTO);

        ResponseEntity<CardapioResponseDTO> response =
                controller.atualizarCardapio(id, updateDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo(responseDTO.getNome());
        assertThat(response.getBody().getDescricao()).isEqualTo(responseDTO.getDescricao());
        verify(atualizarCardapioUseCase).atualizarCardapio(cardapioDominio, id);
    }

    @Test
    void devePropagarCardapioNaoEncontradoException_aoAtualizar() {
        UUID id = UUID.randomUUID();
        var updateDTO = criarCardapioUpdate();
        var cardapioDominio = criarCardapioDominio(updateDTO);

        cardapioMapperMock.when(() -> CardapioMapper.paraDominioDeDtoUpdate(updateDTO))
                .thenReturn(cardapioDominio);
        when(atualizarCardapioUseCase.atualizarCardapio(cardapioDominio, id))
                .thenThrow(new CardapioNaoEncontradoException());

        assertThrows(CardapioNaoEncontradoException.class,
                () -> controller.atualizarCardapio(id, updateDTO));
        verify(atualizarCardapioUseCase).atualizarCardapio(cardapioDominio, id);
    }

    @Test
    void deveDeletarCardapio_Retornar204() {
        UUID id = UUID.randomUUID();
        doNothing().when(deletarCardapioUseCase).deletarCardapio(id);

        ResponseEntity<Void> response = controller.deletarCardapio(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(deletarCardapioUseCase).deletarCardapio(id);
    }

    @Test
    void devePropagarCardapioNaoEncontradoException_aoDeletar() {
        UUID id = UUID.randomUUID();
        doThrow(new CardapioNaoEncontradoException())
                .when(deletarCardapioUseCase).deletarCardapio(id);

        assertThrows(CardapioNaoEncontradoException.class,
                () -> controller.deletarCardapio(id));
        verify(deletarCardapioUseCase).deletarCardapio(id);
    }


    private CardapioRequestDTO criarCardapioRequest() {
        CardapioRequestDTO request = new CardapioRequestDTO();
        request.setNome("Macaxeira com charque");
        request.setDescricao("Macaxeira com charque acebolada");
        request.setPreco(25.0);
        return request;
    }

    private CardapioUpdateDTO criarCardapioUpdate() {
        CardapioUpdateDTO updateDTO = new CardapioUpdateDTO();
        updateDTO.setNome("Macaxeira com charque");
        updateDTO.setDescricao("Macaxeira com charque acebolada");
        updateDTO.setPreco(35.0);
        return updateDTO;
    }

    private Cardapio criarCardapioDominio(CardapioRequestDTO request) {
        return CardapioMapper.paraDominioDeDto(request);
    }

    private Cardapio criarCardapioDominio(CardapioUpdateDTO updateDTO) {
        return CardapioMapper.paraDominioDeDtoUpdate(updateDTO);
    }

    private CardapioResponseDTO criarCardapioResponse() {
        CardapioResponseDTO responseDTO = new CardapioResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setNome("Macaxeira com charque");
        responseDTO.setDescricao("Macaxeira com charque acebolada");
        responseDTO.setPreco(35.0);
        return responseDTO;
    }

    private Cardapio criarCardapioCriado(CardapioRequestDTO requestDTO) {
        return criarCardapioDominio(requestDTO);
    }
}
