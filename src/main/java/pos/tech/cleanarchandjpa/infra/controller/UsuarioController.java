package pos.tech.cleanarchandjpa.infra.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.tech.cleanarchandjpa.core.domain.ParametrosPag;
import pos.tech.cleanarchandjpa.core.dto.PaginacaoOutput;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioJaExisteException;
import pos.tech.cleanarchandjpa.core.usecase.usuario.AtualizarUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.usuario.CriarUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.usuario.DeletarUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.usuario.ListaDeUsuariosUseCase;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioRequestDTO;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioResponseDTO;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioUpdateDTO;

import java.util.UUID;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final ListaDeUsuariosUseCase listaDeUsuariosUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase, ListaDeUsuariosUseCase listaDeUsuariosUseCase, AtualizarUsuarioUseCase atualizarUsuarioUseCase, DeletarUsuarioUseCase deletarUsuarioUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.listaDeUsuariosUseCase = listaDeUsuariosUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) throws UsuarioJaExisteException, DadosInvalidosException {
        var usuario = UsuarioMapper.toDomainDto(usuarioRequestDTO);
        var usuarioCadastrado = criarUsuarioUseCase.cadastrarUsuario(usuario);
        UsuarioResponseDTO response = UsuarioMapper.toDTODto(usuarioCadastrado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public PaginacaoOutput<UsuarioResponseDTO> listarTodosOsUsuarios(ParametrosPag parametrosPag) {
        var usuarios = listaDeUsuariosUseCase.listarUsuarios(parametrosPag);
        return PaginacaoMapper.fromDto(usuarios);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@RequestBody UsuarioUpdateDTO usuarioUpdateDTO) {
        var usuario  = UsuarioMapper.toDomainDtoUp(usuarioUpdateDTO);
        var usuarioAtualizado = atualizarUsuarioUseCase.atualizarUsuario(usuario);
        UsuarioResponseDTO response = UsuarioMapper.toDTODto(usuarioAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        deletarUsuarioUseCase.excluirUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
