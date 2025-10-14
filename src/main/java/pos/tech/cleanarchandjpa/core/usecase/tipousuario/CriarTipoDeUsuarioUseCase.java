package pos.tech.cleanarchandjpa.core.usecase.tipousuario;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;

import java.util.UUID;

public class CriarTipoDeUsuarioUseCase {

    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final UsuarioGateway usuarioGateway;

    public CriarTipoDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public TipoUsuario criarUsuario(UUID usuarioId, TipoUsuario dominio) {
        var usuario = usuarioGateway.buscarUsuario(usuarioId);
        var tipoUsuario = dominio.atribuirTipoUsuario(usuario);
        return tipoUsuarioGateway.salvar(tipoUsuario);
    }
}
