package pos.tech.cleanarchandjpa.core.gateway;

import pos.tech.cleanarchandjpa.core.domain.Usuario;

import java.util.Optional;

public interface UsuarioGateway {
    Optional<Usuario> criarUsuario(Usuario usuario);
    Optional<Usuario> buscarUsuarioPorCpf(Usuario usuario);
    Optional<Usuario> buscarUsuarioPorCnpj(Usuario usuario);

}
