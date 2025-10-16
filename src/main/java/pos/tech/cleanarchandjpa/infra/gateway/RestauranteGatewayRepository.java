package pos.tech.cleanarchandjpa.infra.gateway;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.RestauranteMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.RestauranteRepository;

import java.util.UUID;

@Repository
@Transactional
public class RestauranteGatewayRepository implements RestauranteGateway {
    private final RestauranteRepository restauranteRepository;

    public RestauranteGatewayRepository(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public Restaurante criarRestaurante(Restaurante restauranteComUsuario) {
        var entidade = RestauranteMapper.paraEntidade(restauranteComUsuario);
        var  restaurante = restauranteRepository.save(entidade);
        return RestauranteMapper.paraDominio(restaurante);
    }

    @Override
    public PaginacaoResult<Restaurante> buscarRestaurantesAbertos(ParametrosPag parametrosPag) {
        var paginacao = PaginacaoMapper.deParametrosPagParaPageable(parametrosPag);
        var page = restauranteRepository.findAll(paginacao);
        return PaginacaoMapper.dePageParaPaginacaoRestaurante(page);
    }

    @Override
    public Restaurante buscarRestaurantePeloId(UUID id) {
        return restauranteRepository.findById(id)
                .map(RestauranteMapper::paraDominio)
                .orElse(null);
    }

    @Override
    public Restaurante salvar(Restaurante restauranteAchado) {
        var entidade = RestauranteMapper.paraEntidade(restauranteAchado);
        var restaurante = restauranteRepository.save(entidade);
        return RestauranteMapper.paraDominioOptional(restaurante);
    }

    @Override
    public void deletar(UUID id) {
        restauranteRepository.deleteById(id);
    }
}
