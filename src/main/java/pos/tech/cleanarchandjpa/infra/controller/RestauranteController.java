package pos.tech.cleanarchandjpa.infra.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteUpdateDTO;
import pos.tech.cleanarchandjpa.core.usecase.restaurante.*;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.RestauranteMapper;

import java.util.UUID;

@RestController
@RequestMapping("/restaurante")
public class RestauranteController {

    private final CriarRestauranteUsecase criarRestauranteUsecase;
    private final ListarRestauranteUseCase listarRestauranteUsecase;
    private final DeletarRestauranteUseCase deletarRestauranteUseCase;
    private final AtualizarRestauranteComEnderecoUseCase atualizarRestauranteComEnderecoUseCase;

    public RestauranteController(CriarRestauranteUsecase criarRestauranteUsecase, ListarRestauranteUseCase listarRestauranteUsecase, DeletarRestauranteUseCase deletarRestauranteUseCase, AtualizarRestauranteComEnderecoUseCase atualizarRestauranteComEnderecoUseCase) {
        this.criarRestauranteUsecase = criarRestauranteUsecase;
        this.listarRestauranteUsecase = listarRestauranteUsecase;
        this.deletarRestauranteUseCase = deletarRestauranteUseCase;
        this.atualizarRestauranteComEnderecoUseCase = atualizarRestauranteComEnderecoUseCase;
    }

    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> criarRestaurante(@RequestBody @Valid RestauranteRequestDTO requestDTO){
        var restaurante = RestauranteMapper.paraDominioDeRequest(requestDTO);
        var novoRestaurante =  criarRestauranteUsecase.criarRestaurante(restaurante);
        var response =  RestauranteMapper.paraResponseDTO(novoRestaurante);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PaginacaoResult<RestauranteResponseDTO>> listarRestaurantes(Pageable pageable){
        var parametrosPag = PaginacaoMapper.fromPageable(pageable);
        var resturantes = listarRestauranteUsecase.listarRestaurantes(parametrosPag);
        var dto = PaginacaoMapper.fromDtoR(resturantes);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizarRestaurante(@PathVariable("id") UUID id, @Valid @RequestBody RestauranteUpdateDTO updateDTO){
        var dominio =  RestauranteMapper.paraDominioDeDTO(updateDTO);
        var restaurante = atualizarRestauranteComEnderecoUseCase.atualizarRestauranteComEndereco(dominio, id);
        var response = RestauranteMapper.paraResponseDTO(restaurante);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable("id") UUID id){
        deletarRestauranteUseCase.deletarRestaurante(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
