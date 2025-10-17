package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioUpdateDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.CardapioEntity;

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
                dto.getNome(),
                dto.getDescricao(),
                dto.getPreco(),
                dto.getCaminhoFoto()
        );
    }

    public static CardapioResponseDTO paraResponseDeDominio(Cardapio cardapio) {
        return new CardapioResponseDTO(
                cardapio.getId(),
                cardapio.getNome(),
                cardapio.getDescricao(),
                cardapio.getPreco()
        );
    }

    public static CardapioEntity paraEntidade(Cardapio cardapio) {
        var restauranteEntidade = RestauranteMapper.paraEntidade(cardapio.getRestaurante());
        return new CardapioEntity(
                cardapio.getId(),
                cardapio.getNome(),
                cardapio.getDescricao(),
                cardapio.getPreco(),
                cardapio.getCaminhoImagem(),
                restauranteEntidade
        );
    }

    public static Cardapio paraDominioDeEntidadeOptional(CardapioEntity cardapioSalvo) {
        return paraDominioDeEntidade(cardapioSalvo);
    }

    public static Cardapio paraDominioDeEntidade(CardapioEntity cardapioSalvo) {
        var restauranteDominio = RestauranteMapper.paraDominioOptional(cardapioSalvo.getRestaurante());
        return new Cardapio(
                cardapioSalvo.getId(),
                cardapioSalvo.getNome(),
                cardapioSalvo.getDescricao(),
                cardapioSalvo.getPreco(),
                cardapioSalvo.getCaminhoImagem(),
                restauranteDominio
        );
    }

}
