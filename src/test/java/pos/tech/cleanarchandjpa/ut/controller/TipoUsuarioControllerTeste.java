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
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioUpdateDTO;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.TipoUsuarioNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.AtualizarTiposDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.CriarTipoDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.DeletarTipoDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.ListarTiposDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.TipoUsuarioMapper;
import pos.tech.cleanarchandjpa.infra.http.controller.TipoUsuarioController;
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

class TipoUsuarioControllerTest {

    @Mock
    CriarTipoDeUsuarioUseCase criarTipoDeUsuarioUseCase;

    @Mock
    AtualizarTiposDeUsuarioUseCase atualizarTiposDeUsuarioUseCase;

    @Mock
    ListarTiposDeUsuarioUseCase listarTiposDeUsuarioUseCase;

    @Mock
    DeletarTipoDeUsuarioUseCase deletarTiposDeUsuarioUseCase;

    TipoUsuarioController controller;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    AutoCloseable openMocks;

    MockedStatic<TipoUsuarioMapper> tipoUsuarioMapperMock;
    MockedStatic<PaginacaoMapper> paginacaoMapperMock;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        controller = new TipoUsuarioController(
                criarTipoDeUsuarioUseCase,
                atualizarTiposDeUsuarioUseCase,
                listarTiposDeUsuarioUseCase,
                deletarTiposDeUsuarioUseCase
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

        tipoUsuarioMapperMock = mockStatic(TipoUsuarioMapper.class);
        paginacaoMapperMock = mockStatic(PaginacaoMapper.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (tipoUsuarioMapperMock != null) tipoUsuarioMapperMock.close();
        if (paginacaoMapperMock != null) paginacaoMapperMock.close();
        openMocks.close();
    }

    @Test
    void deveCriarTipoUsuario_Retornar201_ComBody() throws Exception {
        UUID usuarioId = UUID.randomUUID();
        TipoUsuarioRequestDTO request = criarTipoUsuarioRequest();
        TipoUsuario tipoUsuarioDominio = criarTipoUsuarioDominio(request);
        TipoUsuario tipoUsuarioCriado = criarTipoUsuarioCriado(request);
        TipoUsuarioResponseDTO responseDTO = criarTipoUsuarioResponse();

        tipoUsuarioMapperMock.when(() -> TipoUsuarioMapper.paraDominioDeDto(request))
                .thenReturn(tipoUsuarioDominio);
        when(criarTipoDeUsuarioUseCase.criarUsuario(usuarioId, tipoUsuarioDominio))
                .thenReturn(tipoUsuarioCriado);
        tipoUsuarioMapperMock.when(() -> TipoUsuarioMapper.paraResponseDeDominio(tipoUsuarioCriado))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/tipoUsuario/" + usuarioId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)));

        verify(criarTipoDeUsuarioUseCase).criarUsuario(usuarioId, tipoUsuarioDominio);
    }

    @Test
    void devePropagarUsuarioNaoEncontradoException_aoCriar() {
        UUID usuarioId = UUID.randomUUID();
        var request = criarTipoUsuarioRequest();
        var tipoUsuarioDominio = criarTipoUsuarioDominio(request);

        tipoUsuarioMapperMock.when(() -> TipoUsuarioMapper.paraDominioDeDto(request))
                .thenReturn(tipoUsuarioDominio);
        when(criarTipoDeUsuarioUseCase.criarUsuario(usuarioId, tipoUsuarioDominio))
                .thenThrow(new UsuarioNaoEncontradoException());

        assertThrows(UsuarioNaoEncontradoException.class,
                () -> controller.criarTipoUsuario(usuarioId, request));
        verify(criarTipoDeUsuarioUseCase).criarUsuario(usuarioId, tipoUsuarioDominio);
    }

    @Test
    void devePropagarDadosInvalidosException_aoCriar() {
        UUID usuarioId = UUID.randomUUID();
        var request = criarTipoUsuarioRequestDadosInvalidos();
        var tipoUsuarioDominio = criarTipoUsuarioDominio(request);

        tipoUsuarioMapperMock.when(() -> TipoUsuarioMapper.paraDominioDeDto(request))
                .thenReturn(tipoUsuarioDominio);
        when(criarTipoDeUsuarioUseCase.criarUsuario(usuarioId, tipoUsuarioDominio))
                .thenThrow(new DadosInvalidosException("Dados inválidos"));

        assertThrows(DadosInvalidosException.class,
                () -> controller.criarTipoUsuario(usuarioId, request));
        verify(criarTipoDeUsuarioUseCase).criarUsuario(usuarioId, tipoUsuarioDominio);
    }

    @Test
    void deveListarTiposUsuario_Retornar200_ComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);

        var parametros = new ParametrosPag(0, 10, null, null);
        paginacaoMapperMock.when(() -> PaginacaoMapper.dePageableParaParametrosPag(pageable))
                .thenReturn(parametros);

        @SuppressWarnings("unchecked")
        var paginaDominio = mock(PaginacaoResult.class);
        var paginaResponse = mock(PaginacaoResult.class);

        when(listarTiposDeUsuarioUseCase.listarTipoDeUsuarios(parametros))
                .thenReturn(paginaDominio);

        paginacaoMapperMock.when(() -> PaginacaoMapper.paraResponsePaginacaoTipoUsuario(paginaDominio))
                .thenReturn(paginaResponse);

        ResponseEntity<PaginacaoResult<TipoUsuarioResponseDTO>> response =
                controller.listarTipoUsuario(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(listarTiposDeUsuarioUseCase).listarTipoDeUsuarios(parametros);
    }

    @Test
    void deveAtualizarTipoUsuario_Retornar200_ComBody() {
        UUID id = UUID.randomUUID();
        var updateDTO = criarTipoUsuarioUpdate();
        var tipoUsuarioDominio = criarTipoUsuarioDominio(id, updateDTO);
        var tipoUsuarioAtualizado = tipoUsuarioDominio;
        var responseDTO = criarTipoUsuarioResponse();

        tipoUsuarioMapperMock.when(() -> TipoUsuarioMapper.paraDominioDeDtoUpdate(id, updateDTO))
                .thenReturn(tipoUsuarioDominio);
        when(atualizarTiposDeUsuarioUseCase.atualizarUsuario(tipoUsuarioDominio, id))
                .thenReturn(tipoUsuarioAtualizado);
        tipoUsuarioMapperMock.when(() -> TipoUsuarioMapper.paraResponseDeDominio(tipoUsuarioAtualizado))
                .thenReturn(responseDTO);

        ResponseEntity<TipoUsuarioResponseDTO> response =
                controller.atualizarTipoUsuario(id, updateDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(responseDTO.getId());
        assertThat(response.getBody().getTipoUsuario()).isEqualTo(responseDTO.getTipoUsuario());
        verify(atualizarTiposDeUsuarioUseCase).atualizarUsuario(tipoUsuarioDominio, id);
    }

    @Test
    void devePropagarTipoUsuarioNaoEncontradoException_aoAtualizar() {
        UUID id = UUID.randomUUID();
        var updateDTO = criarTipoUsuarioUpdate();
        var tipoUsuarioDominio = criarTipoUsuarioDominio(id, updateDTO);

        tipoUsuarioMapperMock.when(() -> TipoUsuarioMapper.paraDominioDeDtoUpdate(id, updateDTO))
                .thenReturn(tipoUsuarioDominio);
        when(atualizarTiposDeUsuarioUseCase.atualizarUsuario(tipoUsuarioDominio, id))
                .thenThrow(new TipoUsuarioNaoEncontradoException());

        assertThrows(TipoUsuarioNaoEncontradoException.class,
                () -> controller.atualizarTipoUsuario(id, updateDTO));
        verify(atualizarTiposDeUsuarioUseCase).atualizarUsuario(tipoUsuarioDominio, id);
    }

    @Test
    void deveDeletarTipoUsuario_Retornar204() {
        UUID id = UUID.randomUUID();
        doNothing().when(deletarTiposDeUsuarioUseCase).deletarTipoUsuario(id);

        ResponseEntity<Void> response = controller.deletarTipoUsuario(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(deletarTiposDeUsuarioUseCase).deletarTipoUsuario(id);
    }

    @Test
    void devePropagarTipoUsuarioNaoEncontradoException_aoDeletar() {
        UUID id = UUID.randomUUID();
        doThrow(new TipoUsuarioNaoEncontradoException())
                .when(deletarTiposDeUsuarioUseCase).deletarTipoUsuario(id);

        assertThrows(TipoUsuarioNaoEncontradoException.class,
                () -> controller.deletarTipoUsuario(id));
        verify(deletarTiposDeUsuarioUseCase).deletarTipoUsuario(id);
    }

    private TipoUsuarioRequestDTO criarTipoUsuarioRequest() {
        TipoUsuarioRequestDTO request = new TipoUsuarioRequestDTO();
        request.setTipoUsuario("Cliente");
        return request;
    }

    private TipoUsuarioRequestDTO criarTipoUsuarioRequestDadosInvalidos() {
        TipoUsuarioRequestDTO request = new TipoUsuarioRequestDTO();
        request.setTipoUsuario("");
        return request;
    }

    private TipoUsuarioUpdateDTO criarTipoUsuarioUpdate() {
        TipoUsuarioUpdateDTO updateDTO = new TipoUsuarioUpdateDTO();
        updateDTO.setTipoUsuario("Cliente Premium");
        return updateDTO;
    }

    private TipoUsuario criarTipoUsuarioDominio(TipoUsuarioRequestDTO request) {
        return TipoUsuarioMapper.paraDominioDeDto(request);
    }

    private TipoUsuario criarTipoUsuarioDominio(UUID id, TipoUsuarioUpdateDTO updateDTO) {
        return TipoUsuarioMapper.paraDominioDeDtoUpdate(id, updateDTO);
    }

    private TipoUsuarioResponseDTO criarTipoUsuarioResponse() {
        List<UUID> ids = new ArrayList<>();
        ids.add(UUID.randomUUID());
        ids.add(UUID.randomUUID());
        TipoUsuarioResponseDTO responseDTO = new TipoUsuarioResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setTipoUsuario("Cliente");
        responseDTO.setIdsUsuarios(ids);
        return responseDTO;
    }

    private TipoUsuario criarTipoUsuarioCriado(TipoUsuarioRequestDTO requestDTO) {
        return criarTipoUsuarioDominio(requestDTO);
    }
}