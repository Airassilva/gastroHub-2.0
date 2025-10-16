package pos.tech.cleanarchandjpa.infra.http.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
@Validated
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final ListaDeUsuariosUseCase listaDeUsuariosUseCase;
    private final AtualizarUsuarioComEnderecoUseCase atualizarUsuarioComEnderecoUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) throws UsuarioJaExisteException, DadosInvalidosException {
        var usuario = UsuarioMapper.paraDominioDeDto(usuarioRequestDTO);
        var usuarioCadastrado = criarUsuarioUseCase.cadastrarUsuario(usuario);
        var dto = UsuarioMapper.paraResponseDeDomain(usuarioCadastrado);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResult<UsuarioResponseDTO>> listarTodosOsUsuarios(Pageable pageable) {
        var parametrosPag = PaginacaoMapper.dePageableParaParametrosPag(pageable);
        var usuarios = listaDeUsuariosUseCase.listarUsuarios(parametrosPag);
        var pageDto =  PaginacaoMapper.paraResponsePaginacao(usuarios);
        return ResponseEntity.ok(pageDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable("id") UUID id, @Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        var usuario  = UsuarioMapper.paraDominioDeDtoUpdate(usuarioUpdateDTO);
        var usuarioAtualizado = atualizarUsuarioComEnderecoUseCase.atualizarUsuarioComEndereco(usuario, id);
        var response =  UsuarioMapper.paraResponseDeDomain(usuarioAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") UUID id) {
        deletarUsuarioUseCase.excluirUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
