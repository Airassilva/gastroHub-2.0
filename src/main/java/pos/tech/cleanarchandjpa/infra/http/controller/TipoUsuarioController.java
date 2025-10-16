package pos.tech.cleanarchandjpa.infra.http.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioUpdateDTO;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.AtualizarTiposDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.CriarTipoDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.DeletarTipoDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.ListarTiposDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.TipoUsuarioMapper;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tipoUsuario")
@Validated
public class TipoUsuarioController {

    private final CriarTipoDeUsuarioUseCase criarTipoDeUsuarioUseCase;
    private final AtualizarTiposDeUsuarioUseCase atualizarTiposDeUsuarioUseCase;
    private final ListarTiposDeUsuarioUseCase listarTiposDeUsuarioUseCase;
    private final DeletarTipoDeUsuarioUseCase deletarTiposDeUsuarioUseCase;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<TipoUsuarioResponseDTO> criarTipoUsuario(@PathVariable UUID usuarioId, @Valid @RequestBody TipoUsuarioRequestDTO requestDTO){
        var dominio = TipoUsuarioMapper.paraDominioDeDto(requestDTO);
        var tipoUsuarioCriado = criarTipoDeUsuarioUseCase.criarUsuario(usuarioId, dominio);
        var response = TipoUsuarioMapper.paraResponseDeDominio(tipoUsuarioCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResult<TipoUsuarioResponseDTO>> listarTipoUsuario(Pageable pageable){
        var parametrosPag = PaginacaoMapper.dePageableParaParametrosPag(pageable);
        var usuarios = listarTiposDeUsuarioUseCase.listarTipoDeUsuarios(parametrosPag);
        var response = PaginacaoMapper.paraResponsePaginacaoTipoUsuario(usuarios);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> atualizarTipoUsuario(@PathVariable("id") UUID id, @Valid @RequestBody TipoUsuarioUpdateDTO updateDTO){
        var dominio = TipoUsuarioMapper.paraDominioDeDtoUpdate(id,updateDTO);
        var tipoUsuario = atualizarTiposDeUsuarioUseCase.atualizarUsuario(dominio, id);
        var response = TipoUsuarioMapper.paraResponseDeDominio(tipoUsuario);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTipoUsuario(@PathVariable("id") UUID id){
        deletarTiposDeUsuarioUseCase.deletarTipoUsuario(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
