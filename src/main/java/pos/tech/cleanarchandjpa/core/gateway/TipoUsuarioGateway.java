package pos.tech.cleanarchandjpa.core.gateway;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;

import java.util.UUID;

public interface TipoUsuarioGateway {
    TipoUsuario salvar(TipoUsuario tipoUsuario);

    PaginacaoResult<TipoUsuario> listarTiposDeUsuarios(Usuario usuario, ParametrosPag parametrosPag);

    TipoUsuario buscarTipoUsuario(UUID id);

    void deletarUsuario(UUID id);
}
