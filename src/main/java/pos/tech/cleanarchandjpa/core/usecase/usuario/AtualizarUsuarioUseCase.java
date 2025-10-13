package pos.tech.cleanarchandjpa.core.usecase.usuario;

import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.UsuarioOutput;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;

public class AtualizarUsuarioUseCase {
    private final UsuarioGateway usuarioGateway;

    public AtualizarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public UsuarioOutput atualizarUsuario(Usuario usuario) {
        var usuarioAtualizado = usuarioGateway.buscarUsuario(usuario);
        return UsuarioMapper.toOutPutDTO(usuarioAtualizado);
    }
}
