package pos.tech.cleanarchandjpa.infra.gateway;

import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.UsuarioRepository;

import java.util.Optional;

public class UsuarioRepositoryGateway implements UsuarioGateway {
    private final UsuarioRepository usuarioRepository;

    public UsuarioRepositoryGateway(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> criarUsuario(Usuario usuario) {
        var novoUsuario = UsuarioMapper.toEntity(usuario);
        usuarioRepository.save(novoUsuario);
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorCpf(Usuario usuario) {
        var novoUsuario = UsuarioMapper.toEntity(usuario);
        usuarioRepository.findByCpf(novoUsuario.getCpf());
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorCnpj(Usuario usuario) {
        var novoUsuario = UsuarioMapper.toEntity(usuario);
        usuarioRepository.findByCnpj(novoUsuario.getCnpj());
        return Optional.empty();
    }
}
