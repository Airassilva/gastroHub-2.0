package pos.tech.cleanarchandjpa.core.usecase.usuario;

import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.usecase.AtualizarEnderecoUseCase;

import java.util.UUID;

public class AtualizarUsuarioComEnderecoUseCaseImpl implements AtualizarUsuarioComEnderecoUseCase {
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final AtualizarEnderecoUseCase atualizarEnderecoUseCase;

    public AtualizarUsuarioComEnderecoUseCaseImpl(AtualizarUsuarioUseCase atualizarUsuarioUseCase, AtualizarEnderecoUseCase atualizarEnderecoUseCase) {
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.atualizarEnderecoUseCase = atualizarEnderecoUseCase;
    }

    @Override
    public Usuario atualizarUsuarioComEndereco(Usuario usuario, UUID id) {
        var usuarioAtualizado = atualizarUsuarioUseCase.atualizarUsuario(usuario, id);
        atualizarEnderecoUseCase.atualizarEnderecoSeNecessario(usuarioAtualizado, usuario);
        return usuarioAtualizado;
    }
}
