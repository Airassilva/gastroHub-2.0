package pos.tech.cleanarchandjpa.core.usecase.usuario;

import pos.tech.cleanarchandjpa.core.domain.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.domain.ParametrosPag;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;

public class ListaDeUsuariosUseCase {
    private final UsuarioGateway usuarioGateway;

    public ListaDeUsuariosUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public PaginacaoResult<Usuario> listarUsuarios(ParametrosPag parametrosPag) {
        return usuarioGateway.buscarTodosOsUsuarios(parametrosPag);
    }
}
