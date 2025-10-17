package pos.tech.cleanarchandjpa.core.usecase.restaurante;

import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.exception.RestauranteNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;

import java.util.UUID;

public class AtualizarRestauranteUseCase {

    private final RestauranteGateway restauranteGateway;

    public AtualizarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public Restaurante atualizarRestaurante(Restaurante restaurante, UUID id) {
        var restauranteAchado = restauranteGateway.buscarRestaurantePeloId(id);
        if(restauranteAchado == null){
            throw new RestauranteNaoEncontradoException();
        }
        restauranteAchado.atualizarDados(restaurante);
        return restauranteGateway.salvar(restauranteAchado);
    }
}
