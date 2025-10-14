package pos.tech.cleanarchandjpa.infra.gateway;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.UsuarioRepository;

import java.util.List;
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
        return Optional.ofNullable(UsuarioMapper.paraDominio(usuarioEncontrado));
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
        Sort.Direction direction = "DESC".equalsIgnoreCase(parametrosPag.sortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                parametrosPag.page(),
                parametrosPag.pageSize(),
                Sort.by(direction, parametrosPag.sortBy())
        );

        Page<UsuarioEntity> page = usuarioRepository.findAllAtivo(pageable);

        List<Usuario> usuarios = page.getContent().stream()
                .map(UsuarioMapper::paraDominio)
                .toList();

        return new PaginacaoResult<>(
                usuarios,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    @Override
    public Usuario buscarUsuario(UUID id) {
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
        return UsuarioMapper.paraDominio(usuario);
    }

    @Override
    public void deletarUsuario(UUID id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario salvarUsuario(Usuario usuarioExistente) {
       UsuarioEntity usuarioEntity = UsuarioMapper.paraEntidade(usuarioExistente);
       UsuarioEntity salvo = usuarioRepository.save(usuarioEntity);
       return UsuarioMapper.paraDominio(salvo);
    }
}
