package pos.tech.cleanarchandjpa.core.gateway;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;

import java.util.UUID;

public interface TipoUsuarioGateway {
    TipoUsuario salvar(TipoUsuario tipoUsuario);
    PaginacaoResult<TipoUsuario> listarTiposDeUsuarios(ParametrosPag parametrosPag);
    TipoUsuario buscarTipoUsuario(UUID id);
    void deletarUsuario(UUID id);

    TipoUsuario buscarPeloId(UUID id);
}
