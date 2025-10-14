package pos.tech.cleanarchandjpa.infra.gateway;

import jakarta.persistence.EntityNotFoundException;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.RestauranteMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.RestauranteRepository;

import java.util.UUID;

public class RestauranteGatewayRepository implements RestauranteGateway {
    private final RestauranteRepository restauranteRepository;

    public RestauranteGatewayRepository(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public Restaurante criarRestaurante(Restaurante restauranteComUsuario) {
        var entidade = RestauranteMapper.paraEntidade(restauranteComUsuario);
        var restaurante = restauranteRepository.save(entidade);
        return RestauranteMapper.paraDominio(restaurante);
    }

    @Override
    public PaginacaoResult<Restaurante> buscarRestaurantesAbertos(ParametrosPag parametrosPag) {
        var paginacao = PaginacaoMapper.toPageable(parametrosPag);
        var page = restauranteRepository.findAll(paginacao);
        return PaginacaoMapper.fromEntityPage(page);
    }

    @Override
    public Restaurante buscarRestaurantePeloId(UUID id) {
        var restaurante = restauranteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
        return RestauranteMapper.paraDominio(restaurante);
    }

    @Override
    public Restaurante salvar(Restaurante restauranteAchado) {
        var entidade = RestauranteMapper.paraEntidade(restauranteAchado);
        var restaurante = restauranteRepository.save(entidade);
        return RestauranteMapper.paraDominio(restaurante);
    }

    @Override
    public void deletar(UUID id) {
        restauranteRepository.deleteById(id);
    }
}
