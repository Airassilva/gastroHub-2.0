package pos.tech.cleanarchandjpa.core.gateway;

import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {
    Optional<Usuario> criarUsuario(Usuario usuario);
    Optional<Usuario> buscarUsuarioPorCpf(Usuario usuario);
    Optional<Usuario> buscarUsuarioPorCnpj(Usuario usuario);
    PaginacaoResult<Usuario> buscarTodosOsUsuarios(ParametrosPag parametrosPag);
    Usuario buscarUsuario(UUID usuario);
    void deletarUsuario(UUID id);

    Usuario salvarUsuario(Usuario usuarioExistente);
}
