package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioUpdateDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.CardapioEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.DisponibilidadeConsumo;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardapioMapper {

    public static Cardapio paraDominioDeDto(CardapioRequestDTO dto) {
        return new Cardapio(
                dto.getNome(),
                dto.getDescricao(),
                dto.getPreco(),
                dto.getCaminhoFoto()
        );
    }

    public static Cardapio paraDominioDeDtoUpdate(CardapioUpdateDTO dto) {
        return new Cardapio(
                dto.nome(),
                dto.descricao(),
                dto.preco(),
                dto.caminhoFoto()
        );
    }

    public static CardapioResponseDTO paraResponseDeDominio(Cardapio cardapio) {
        return new CardapioResponseDTO(
                cardapio.getId(),
                cardapio.getNome(),
                cardapio.getDescricao()
        );
    }

    public static CardapioEntity paraEntidade(Cardapio cardapio) {
        var restauranteEntidade = RestauranteMapper.paraEntidade(cardapio.getRestaurante());
        return new CardapioEntity(
                cardapio.getId(),
                cardapio.getNome(),
                cardapio.getPreco(),
                cardapio.getDescricao(),
                DisponibilidadeConsumo.APENAS_NO_RESTAURANTE,
                cardapio.getCaminhoImagem(),
                restauranteEntidade
        );
    }

    public static Cardapio paraDominioDeEntidadeOptional(Optional<CardapioEntity> cardapioSalvo) {
        var restauranteDominio = RestauranteMapper.paraDominioOptional(Optional.ofNullable(cardapioSalvo.get().getRestaurante()));
        return new Cardapio(
                cardapioSalvo.get().getId(),
                cardapioSalvo.get().getNome(),
                cardapioSalvo.get().getDescricao(),
                cardapioSalvo.get().getPreco(),
                pos.tech.cleanarchandjpa.core.domain.DisponibilidadeConsumo.APENAS_NO_RESTAURANTE,
                cardapioSalvo.get().getCaminhoImagem(),
                restauranteDominio
        );
    }

    public static Cardapio paraDominioDeEntidade(CardapioEntity cardapioSalvo) {
        var restauranteDominio = RestauranteMapper.paraDominioOptional(Optional.ofNullable(cardapioSalvo.getRestaurante()));
        return new Cardapio(
                cardapioSalvo.getId(),
                cardapioSalvo.getNome(),
                cardapioSalvo.getDescricao(),
                cardapioSalvo.getPreco(),
                pos.tech.cleanarchandjpa.core.domain.DisponibilidadeConsumo.APENAS_NO_RESTAURANTE,
                cardapioSalvo.getCaminhoImagem(),
                restauranteDominio
        );
    }

}
