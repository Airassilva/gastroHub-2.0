package pos.tech.cleanarchandjpa.core.gateway;

import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;

import java.util.UUID;

public interface RestauranteGateway {
    Restaurante criarRestaurante(Restaurante restauranteComUsuario);
    PaginacaoResult<Restaurante> buscarRestaurantesAbertos(ParametrosPag parametrosPag);
    Restaurante buscarRestaurantePeloId(UUID id);
    Restaurante salvar(Restaurante restauranteAchado);
    void deletar(UUID id);
}
