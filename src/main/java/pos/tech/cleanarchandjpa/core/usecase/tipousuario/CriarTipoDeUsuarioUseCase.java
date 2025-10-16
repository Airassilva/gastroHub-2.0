package pos.tech.cleanarchandjpa.core.usecase.tipousuario;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.exception.UsuarioNaoEncontradoException;
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

    public TipoUsuario criarUsuario(UUID usuarioId, TipoUsuario tipoUsuario) {
        var usuario = usuarioGateway.buscarUsuario(usuarioId);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException();
        }
        var dominio = tipoUsuarioGateway.salvar(tipoUsuario);
        usuario.atribuirTipoUsuario(dominio);
        usuarioGateway.salvarUsuario(usuario);
        dominio.adicionarUsuario(usuario);
        return dominio;
    }
}
