package pos.tech.cleanarchandjpa.core.usecase.usuario;

import pos.tech.cleanarchandjpa.core.exception.UsuarioNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;

import java.util.UUID;

public class DeletarUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public DeletarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public void excluirUsuario(UUID id) {
        var usuario = usuarioGateway.buscarUsuario(id);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException();
        }
        usuarioGateway.deletarUsuario(id);
    }
}
