package pos.tech.cleanarchandjpa.core.usecase.restaurante;

import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;

public class ListarRestauranteUseCase {
    private final RestauranteGateway restauranteGateway;

    public ListarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        this.restauranteGateway = restauranteGateway;
    }

    public PaginacaoResult<Restaurante> listarRestaurantes(ParametrosPag parametrosPag) {
        return restauranteGateway.buscarRestaurantesAbertos(parametrosPag);
    }
}
