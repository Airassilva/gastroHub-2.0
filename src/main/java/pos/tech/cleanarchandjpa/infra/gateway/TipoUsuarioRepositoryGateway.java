package pos.tech.cleanarchandjpa.infra.gateway;

import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.repository.TipoUsuarioRepository;

public class TipoUsuarioRepositoryGateway implements TipoUsuarioGateway {
    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioRepositoryGateway(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }
}
