package pos.tech.cleanarchandjpa.core.usecase.tipousuario;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;

import java.util.UUID;

public class AtualizarTiposDeUsuarioUseCase {
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public AtualizarTiposDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    public TipoUsuario atualizarUsuario(TipoUsuario dominio, UUID id) {
        var tipoUsuario =  tipoUsuarioGateway.buscarTipoUsuario(id);
        tipoUsuario.comNovosDados(dominio);
        return tipoUsuarioGateway.salvar(tipoUsuario);
    }
}
