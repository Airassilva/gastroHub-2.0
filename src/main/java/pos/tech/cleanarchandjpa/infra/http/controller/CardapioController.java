package pos.tech.cleanarchandjpa.infra.http.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioUpdateDTO;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.AtualizarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.CriarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.DeletarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.ListarCardapioPeloRestauranteUseCase;
import pos.tech.cleanarchandjpa.infra.database.mapper.CardapioMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cardapio")
@Validated
public class CardapioController {

    private final CriarCardapioUseCase criarCardapioUseCase;
    private final AtualizarCardapioUseCase atualizarCardapioUseCase;
    private final ListarCardapioPeloRestauranteUseCase listarCardapioPeloRestauranteUseCase;
    private final DeletarCardapioUseCase deletarCardapioUseCase;

    @PostMapping("/{restauranteId}")
    public ResponseEntity<CardapioResponseDTO> criarCardapio(@PathVariable UUID restauranteId, @Valid @RequestBody CardapioRequestDTO requestDTO){
        var cardapio = CardapioMapper.paraDominioDeDto(requestDTO);
        var cardapioCriado = criarCardapioUseCase.criarCardapio(cardapio, restauranteId);
        var response = CardapioMapper.paraResponseDeDominio(cardapioCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{restauranteId}")
    public ResponseEntity<PaginacaoResult<CardapioResponseDTO>> listarCardapio(@PathVariable UUID restauranteId,Pageable pageable){
        var parametrosPag = PaginacaoMapper.dePageableParaParametrosPag(pageable);
        var cardapioPorRestaurante = listarCardapioPeloRestauranteUseCase.listarCardapioPorRestaurante(restauranteId,parametrosPag);
        var response = PaginacaoMapper.paraResponsePaginacaoCardapio(cardapioPorRestaurante);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardapioResponseDTO> atualizarCardapio(@PathVariable("id") UUID id, @Valid @RequestBody CardapioUpdateDTO updateDTO){
        var dominio =  CardapioMapper.paraDominioDeDtoUpdate(updateDTO);
        var cardapio = atualizarCardapioUseCase.atualizarCardapio(dominio, id);
        var response = CardapioMapper.paraResponseDeDominio(cardapio);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCardapio(@PathVariable("id") UUID id){
        deletarCardapioUseCase.deletarCardapio(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
