package pos.tech.cleanarchandjpa.infra.gateway;

import org.springframework.data.domain.Page;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.UsuarioRepository;

import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositoryGateway implements UsuarioGateway {
    private final UsuarioRepository usuarioRepository;

    public UsuarioRepositoryGateway(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> criarUsuario(Usuario usuario) {
        var novoUsuario = UsuarioMapper.paraEntidade(usuario);
        var usuarioEncontrado = usuarioRepository.save(novoUsuario);
        return Optional.ofNullable(UsuarioMapper.paraDominioDeOptional(Optional.of(usuarioEncontrado)));
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorCpf(Usuario usuario) {
        var novoUsuario = UsuarioMapper.paraEntidade(usuario);
        return usuarioRepository.findByCpf(novoUsuario.getCpf())
                .flatMap(UsuarioMapper::paraDominioOptional);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorCnpj(Usuario usuario) {
        var novoUsuario = UsuarioMapper.paraEntidade(usuario);
        return usuarioRepository.findByCnpj(novoUsuario.getCnpj())
                .flatMap(UsuarioMapper::paraDominioOptional);
    }

    @Override
    public PaginacaoResult<Usuario> buscarTodosOsUsuarios(ParametrosPag parametrosPag) {
        var pageable = PaginacaoMapper.deParametrosPagParaPageable(parametrosPag);
        Page<UsuarioEntity> page = usuarioRepository.findAllAtivo(pageable);
        return PaginacaoMapper.dePageParaPaginacaoUsuario(page);
    }

    @Override
    public Usuario buscarUsuario(UUID id) {
        var usuario = usuarioRepository.findById(id);
        return UsuarioMapper.paraDominioDeOptional(usuario);
    }

    @Override
    public void deletarUsuario(UUID id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario salvarUsuario(Usuario usuarioExistente) {
       UsuarioEntity usuarioEntity = UsuarioMapper.paraEntidade(usuarioExistente);
       UsuarioEntity salvo = usuarioRepository.save(usuarioEntity);
       return UsuarioMapper.paraDominioDeOptional(Optional.of(salvo));
    }
}
