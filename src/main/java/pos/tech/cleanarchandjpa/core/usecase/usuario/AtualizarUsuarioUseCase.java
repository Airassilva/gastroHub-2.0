package pos.tech.cleanarchandjpa.core.usecase.usuario;

import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;

import java.util.UUID;

public class AtualizarUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public AtualizarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario atualizarUsuario(Usuario usuario, UUID id) {
        Usuario usuarioExistente = usuarioGateway.buscarUsuario(id);
        usuarioExistente.atualizarDadosBasicos(usuario);
        return usuarioGateway.salvarUsuario(usuarioExistente);
    }
}
