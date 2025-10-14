package pos.tech.cleanarchandjpa.core.usecase.tipousuario;

import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;

import java.util.UUID;

public class DeletarTipoDeUsuarioUseCase {
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public DeletarTipoDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    public void deletarTipoUsuario(UUID id) {
        tipoUsuarioGateway.deletarUsuario(id);
    }
}
