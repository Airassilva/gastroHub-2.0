package pos.tech.cleanarchandjpa.core.usecase.restaurante;

import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.exception.UsuarioNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;

import java.util.UUID;


public class CriarRestauranteUsecase {

    private final RestauranteGateway restauranteGateway;
    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public CriarRestauranteUsecase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        this.restauranteGateway = restauranteGateway;
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    public Restaurante criarRestaurante(UUID usuarioIdDono, Restaurante restaurante) {
       var usuarioDono = usuarioGateway.buscarUsuario(usuarioIdDono);
       if(usuarioDono == null) {
           throw new UsuarioNaoEncontradoException();
       }
       var tipoUsuario = tipoUsuarioGateway.buscarTipoUsuario(usuarioIdDono);
       if(tipoUsuario == null) {
            throw new UsuarioNaoEncontradoException();
       }
       var restauranteComUsuario = restaurante.comDono(tipoUsuario, usuarioDono);
        restauranteComUsuario.atribuirEndereco(restaurante.getEndereco());
       return restauranteGateway.criarRestaurante(restauranteComUsuario);
    }
}
