package pos.tech.cleanarchandjpa.infra.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioJaExisteException;
import pos.tech.cleanarchandjpa.core.usecase.CriarUsuarioUseCase;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioRequestDTO;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioResponseDTO;


@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase, UsuarioMapper usuarioMapper) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(UsuarioRequestDTO usuarioRequestDTO) throws UsuarioJaExisteException, DadosInvalidosException {
        var usuario = UsuarioMapper.toDomain(usuarioRequestDTO);
        criarUsuarioUseCase.cadastrarUsuario(usuario);
        UsuarioResponseDTO response = UsuarioMapper.toResponseDTO(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
