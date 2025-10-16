package pos.tech.cleanarchandjpa.core.usecase.tipousuario;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;

public class ListarTiposDeUsuarioUseCase {
    private final TipoUsuarioGateway tipoUsuarioGateway;
    public ListarTiposDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    public PaginacaoResult<TipoUsuario> listarTipoDeUsuarios(ParametrosPag parametrosPag) {
        return tipoUsuarioGateway.listarTiposDeUsuarios(parametrosPag);
    }
}
