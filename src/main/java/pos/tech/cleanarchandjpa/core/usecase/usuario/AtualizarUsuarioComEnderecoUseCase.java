package pos.tech.cleanarchandjpa.core.usecase.usuario;

import pos.tech.cleanarchandjpa.core.domain.Usuario;

import java.util.UUID;

public interface AtualizarUsuarioComEnderecoUseCase {
    Usuario atualizarUsuarioComEndereco(Usuario usuario, UUID id);
}
