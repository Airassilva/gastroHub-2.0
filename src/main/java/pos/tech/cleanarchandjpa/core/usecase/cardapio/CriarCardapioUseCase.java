package pos.tech.cleanarchandjpa.core.usecase.cardapio;

import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.gateway.CardapioGateway;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;

import java.util.UUID;

public class CriarCardapioUseCase {
    private final CardapioGateway cardapioGateway;
    private final RestauranteGateway restauranteGateway;

    public CriarCardapioUseCase(CardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
        this.cardapioGateway = cardapioGateway;
        this.restauranteGateway = restauranteGateway;
    }

    public Cardapio criarCardapio(Cardapio cardapio, UUID restauranteId) {
        var restaurante = restauranteGateway.buscarRestaurantePeloId(restauranteId);
        var cardapioComRestaurante = cardapio.comRestaurante(restaurante);
        restaurante.adicionarCardapio(cardapioComRestaurante);
        return cardapioGateway.salvarCardapio(cardapioComRestaurante);
    }
}
