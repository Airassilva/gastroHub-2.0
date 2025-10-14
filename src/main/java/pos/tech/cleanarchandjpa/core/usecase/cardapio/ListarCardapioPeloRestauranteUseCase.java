package pos.tech.cleanarchandjpa.core.usecase.cardapio;

import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.CardapioGateway;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;

import java.util.UUID;

public class ListarCardapioPeloRestauranteUseCase {
    private final CardapioGateway cardapioGateway;
    private final RestauranteGateway restauranteGateway;

    public ListarCardapioPeloRestauranteUseCase(CardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
        this.cardapioGateway = cardapioGateway;
        this.restauranteGateway = restauranteGateway;
    }

    public PaginacaoResult<Cardapio> listarCardapioPorRestaurante(UUID restauranteId, ParametrosPag parametrosPag) {
        var restaurante = restauranteGateway.buscarRestaurantePeloId(restauranteId);
        return cardapioGateway.listarCardapios(restaurante, parametrosPag);
    }
}
