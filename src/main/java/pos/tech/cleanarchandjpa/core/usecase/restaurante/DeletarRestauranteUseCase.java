package pos.tech.cleanarchandjpa.core.usecase.restaurante;

import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;

import java.util.UUID;

public class DeletarRestauranteUseCase {
    private final RestauranteGateway restauranteGateway;

    public DeletarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public void deletarRestaurante(UUID id) {
        restauranteGateway.deletar(id);
    }
}
