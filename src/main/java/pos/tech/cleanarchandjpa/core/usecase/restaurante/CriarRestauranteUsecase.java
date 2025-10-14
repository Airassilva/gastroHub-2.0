package pos.tech.cleanarchandjpa.core.usecase.restaurante;

import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;


public class CriarRestauranteUsecase {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;

    public CriarRestauranteUsecase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public Restaurante criarRestaurante(Restaurante restaurante) {
       var usuarioDono = usuarioGateway.buscarUsuario(restaurante.getUsuario().getId());
        Restaurante restauranteComUsuario = new Restaurante(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getTipoCozinha(),
                restaurante.getEndereco(),
                restaurante.getHorariosFunc(),
                usuarioDono,
                restaurante.getDataCadastro(),
                restaurante.getDataAtualizacao()
        );
        return restauranteGateway.criarRestaurante(restauranteComUsuario);
    }
}
