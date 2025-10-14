package pos.tech.cleanarchandjpa.infra.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioUpdateDTO;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioJaExisteException;
import pos.tech.cleanarchandjpa.core.usecase.usuario.*;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final ListaDeUsuariosUseCase listaDeUsuariosUseCase;
    private final AtualizarUsuarioComEnderecoUseCase atualizarUsuarioComEnderecoUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase, ListaDeUsuariosUseCase listaDeUsuariosUseCase, AtualizarUsuarioComEnderecoUseCase atualizarUsuarioComEnderecoUseCase, DeletarUsuarioUseCase deletarUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.listaDeUsuariosUseCase = listaDeUsuariosUseCase;
        this.atualizarUsuarioComEnderecoUseCase = atualizarUsuarioComEnderecoUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<Optional<UsuarioResponseDTO>> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) throws UsuarioJaExisteException, DadosInvalidosException {
        var usuario = UsuarioMapper.toDomainDto(usuarioRequestDTO);
        var usuarioCadastrado = criarUsuarioUseCase.cadastrarUsuario(usuario);
        var dto = UsuarioMapper.toResponseDtoOptional(Optional.ofNullable(usuarioCadastrado));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResult<UsuarioResponseDTO>> listarTodosOsUsuarios(Pageable pageable) {
        var parametrosPag = PaginacaoMapper.fromPageable(pageable);
        var usuarios = listaDeUsuariosUseCase.listarUsuarios(parametrosPag);
        var pageDto =  PaginacaoMapper.fromDto(usuarios);
        return ResponseEntity.ok(pageDto);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable("id") UUID id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        var usuario  = UsuarioMapper.toDomainDtoUp(usuarioUpdateDTO);
        var usuarioAtualizado = atualizarUsuarioComEnderecoUseCase.atualizarUsuarioComEndereco(usuario, id);
        var response =  UsuarioMapper.toResponseDTO(usuarioAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") UUID id) {
        deletarUsuarioUseCase.excluirUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
