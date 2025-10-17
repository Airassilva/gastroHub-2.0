package pos.tech.cleanarchandjpa.ut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
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
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioUpdateDTO;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioJaExisteException;
import pos.tech.cleanarchandjpa.core.usecase.usuario.AtualizarUsuarioComEnderecoUseCase;
import pos.tech.cleanarchandjpa.core.usecase.usuario.CriarUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.usuario.DeletarUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.usuario.ListaDeUsuariosUseCase;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.http.controller.UsuarioController;
import pos.tech.cleanarchandjpa.infra.http.exceptions.GlobalExceptionHandler;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioControllerTest {

    @Mock
    CriarUsuarioUseCase criarUsuarioUseCase;

    @Mock
    ListaDeUsuariosUseCase listaDeUsuariosUseCase;

    @Mock
    AtualizarUsuarioComEnderecoUseCase atualizarUsuarioComEnderecoUseCase;

    @Mock
    DeletarUsuarioUseCase deletarUsuarioUseCase;

    UsuarioController controller;
    MockMvc mockMvc;
    ObjectMapper objectMapper;

    AutoCloseable openMocks;

    MockedStatic<UsuarioMapper> usuarioMapperMock;
    MockedStatic<PaginacaoMapper> paginacaoMapperMock;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        controller = new UsuarioController(
                criarUsuarioUseCase,
                listaDeUsuariosUseCase,
                atualizarUsuarioComEnderecoUseCase,
                deletarUsuarioUseCase
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


        usuarioMapperMock = mockStatic(UsuarioMapper.class);
        paginacaoMapperMock = mockStatic(PaginacaoMapper.class);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (usuarioMapperMock != null) usuarioMapperMock.close();
        if (paginacaoMapperMock != null) paginacaoMapperMock.close();
        openMocks.close();
    }

    @Test
    void deveCadastrarUsuario_Retornar201_ComBody() throws Exception, UsuarioJaExisteException {
        UsuarioRequestDTO request = criarUsuarioRequest();
        Usuario usuarioDominio = criarUsuarioDominio(request);
        Usuario usuarioCadastrado = criarUsuarioCadastrado(request);
        UsuarioResponseDTO responseDTO = criarUsuarioResponse();

        usuarioMapperMock.when(() -> UsuarioMapper.paraDominioDeDto(request))
                .thenReturn(usuarioDominio);
        when(criarUsuarioUseCase.cadastrarUsuario(usuarioDominio))
                .thenReturn(usuarioCadastrado);
        usuarioMapperMock.when(() -> UsuarioMapper.paraResponseDeDomain(usuarioCadastrado))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/usuario")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId().toString()))
                .andExpect(jsonPath("$.nome").value(responseDTO.getNome()))
                .andExpect(jsonPath("$.email").value(responseDTO.getEmail()));

        verify(criarUsuarioUseCase).cadastrarUsuario(usuarioDominio);
    }

    @Test
    void devePropagarUsuarioJaExisteException_aoCadastrar() throws UsuarioJaExisteException {
        var request = criarUsuarioRequest();
        var usuarioDominio = criarUsuarioDominio(request);

        usuarioMapperMock.when(() -> UsuarioMapper.paraDominioDeDto(request))
                .thenReturn(usuarioDominio);
        when(criarUsuarioUseCase.cadastrarUsuario(usuarioDominio))
                .thenThrow(new UsuarioJaExisteException("Usuário já existe"));

        assertThrows(UsuarioJaExisteException.class,
                () -> controller.cadastrarUsuario(request));
        verify(criarUsuarioUseCase).cadastrarUsuario(usuarioDominio);
    }

    @Test
    void devePropagarDadosInvalidosException_aoCadastrar() throws UsuarioJaExisteException {
        var request = criarUsuarioRequestDadosInvalidos();
        var usuarioDominio = criarUsuarioDominio(request);

        usuarioMapperMock.when(() -> UsuarioMapper.paraDominioDeDto(request))
                .thenReturn(usuarioDominio);
        when(criarUsuarioUseCase.cadastrarUsuario(usuarioDominio))
                .thenThrow(new DadosInvalidosException("Dados inválidos"));

        assertThrows(DadosInvalidosException.class,
                () -> controller.cadastrarUsuario(request));
        verify(criarUsuarioUseCase).cadastrarUsuario(usuarioDominio);
    }

    @Test
    void deveListarUsuarios_Retornar200_ComPaginacao() {
        Pageable pageable = PageRequest.of(0, 10);

        var parametros = new ParametrosPag(0, 10, null, null);
        paginacaoMapperMock.when(() -> PaginacaoMapper.dePageableParaParametrosPag(pageable))
                .thenReturn(parametros);

        @SuppressWarnings("unchecked")
        var paginaDominio = mock(PaginacaoResult.class);
        var paginaResponse = PaginacaoMapper.paraResponsePaginacao(paginaDominio);

        when(listaDeUsuariosUseCase.listarUsuarios(parametros))
                .thenReturn(paginaDominio);

        paginacaoMapperMock.when(() -> PaginacaoMapper.paraResponsePaginacao(paginaDominio))
                .thenReturn(paginaResponse);

        ResponseEntity<PaginacaoResult<UsuarioResponseDTO>> response = controller.listarTodosOsUsuarios(pageable);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(paginaResponse);
        verify(listaDeUsuariosUseCase).listarUsuarios(parametros);
    }

    @Test
    void deveAtualizarUsuario_Retornar200_ComBody() {
        UUID id = UUID.randomUUID();
        var updateDTO = criarUsuarioUpdate();
        var usuarioDominio = criarUsuarioDominio(updateDTO);
        var usuarioAtualizado = usuarioDominio;
        var responseDTO = criarUsuarioResponse();

        usuarioMapperMock.when(() -> UsuarioMapper.paraDominioDeDtoUpdate(updateDTO))
                .thenReturn(usuarioDominio);
        when(atualizarUsuarioComEnderecoUseCase.atualizarUsuarioComEndereco(usuarioDominio, id))
                .thenReturn(usuarioAtualizado);
        usuarioMapperMock.when(() -> UsuarioMapper.paraResponseDeDomain(usuarioAtualizado))
                .thenReturn(responseDTO);

        ResponseEntity<UsuarioResponseDTO> response = controller.atualizarUsuario(id, updateDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(responseDTO);
        verify(atualizarUsuarioComEnderecoUseCase).atualizarUsuarioComEndereco(usuarioDominio, id);
    }

    @Test
    void deveDeletarUsuario_Retornar204() {
        UUID id = UUID.randomUUID();
        doNothing().when(deletarUsuarioUseCase).excluirUsuario(id);

        ResponseEntity<Void> response = controller.deletarUsuario(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(deletarUsuarioUseCase).excluirUsuario(id);
    }

    private UsuarioRequestDTO criarUsuarioRequest() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNome("Any");
        request.setEmail("any@any.com");
        request.setCpf("123.456.789-09");
        request.setLogin("Any");
        request.setSenha("Any");
        return request;
    }

    private UsuarioRequestDTO criarUsuarioRequestDadosInvalidos() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNome("Any");
        request.setEmail("any@any.com");
        request.setCpf("12345678889");
        request.setLogin("Any");
        request.setSenha("Any");
        return request;
    }

    private UsuarioUpdateDTO criarUsuarioUpdate() {
        UsuarioUpdateDTO request = new UsuarioUpdateDTO();
        request.setEmail("any@any.com");
        request.setTelefone("(81) 99922-4651");
        request.setLogin("AnyOther");
        request.setSenha("AnyOther");
        return request;
    }

    private Usuario criarUsuarioDominio( UsuarioRequestDTO request) {
       return UsuarioMapper.paraDominioDeDto(request);
    }

    private Usuario criarUsuarioDominio(UsuarioUpdateDTO updateDTO) {
        return UsuarioMapper.paraDominioDeDtoUpdate(updateDTO);
    }

    private UsuarioResponseDTO criarUsuarioResponse() {
        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setNome("Any");
        responseDTO.setEmail("any@any.com");
        return responseDTO;
    }

    private Usuario criarUsuarioCadastrado(UsuarioRequestDTO requestDTO) throws UsuarioJaExisteException {
       return criarUsuarioDominio(requestDTO);
    }
}
