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
        var restauranteComUsuario = restaurante.comDono(usuarioDono);
        return restauranteGateway.criarRestaurante(restauranteComUsuario);
    }
}
