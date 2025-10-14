package pos.tech.cleanarchandjpa.core.usecase.tipousuario;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;

import java.util.UUID;

public class ListarTiposDeUsuarioUseCase {
    private final TipoUsuarioGateway tipoUsuarioGateway;
    private final UsuarioGateway usuarioGateway;

    public ListarTiposDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public PaginacaoResult<TipoUsuario> listarTipoDeUsuarios(UUID usuarioId, ParametrosPag parametrosPag) {
        var usuario =  usuarioGateway.buscarUsuario(usuarioId);
        return tipoUsuarioGateway.listarTiposDeUsuarios(usuario, parametrosPag);
    }
}
