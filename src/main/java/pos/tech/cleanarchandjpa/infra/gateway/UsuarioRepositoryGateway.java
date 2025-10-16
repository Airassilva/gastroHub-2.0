package pos.tech.cleanarchandjpa.infra.gateway;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.UsuarioRepository;

import java.util.UUID;


@Repository
@Transactional(readOnly = true)
public class UsuarioRepositoryGateway implements UsuarioGateway {
    private final UsuarioRepository usuarioRepository;

    public UsuarioRepositoryGateway(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario criarUsuario(Usuario usuario) {
        var entidade = UsuarioMapper.paraEntidade(usuario);
        var usuarioSalvo = usuarioRepository.save(entidade);
        return UsuarioMapper.paraDominioDeOptional(usuarioSalvo);
    }

    @Override
    public Usuario buscarUsuarioPorCpf(Usuario usuario) {
        var novoUsuario = UsuarioMapper.paraEntidade(usuario);
        return usuarioRepository.findByCpf(novoUsuario.getCpf())
                        .map(UsuarioMapper::paraDominio)
                        .orElse(null);
    }

    @Override
    public Usuario buscarUsuarioPorCnpj(Usuario usuario) {
        var novoUsuario = UsuarioMapper.paraEntidade(usuario);
        return usuarioRepository.findByCnpj(novoUsuario.getCnpj())
                        .map(UsuarioMapper::paraDominio)
                        .orElse(null);
    }

    @Override
    public PaginacaoResult<Usuario> buscarTodosOsUsuarios(ParametrosPag parametrosPag) {
        var pageable = PaginacaoMapper.deParametrosPagParaPageable(parametrosPag);
        Page<UsuarioEntity> page = usuarioRepository.findAllByAtivoTrue(pageable);
        return PaginacaoMapper.dePageParaPaginacaoUsuario(page);
    }

    @Override
    public Usuario buscarUsuario(UUID id) {
         return usuarioRepository.findById(id)
                 .map(UsuarioMapper::paraDominioDeOptional)
                 .orElse(null);
    }

    @Override
    public void deletarUsuario(UUID id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario salvarUsuario(Usuario usuarioExistente) {
       UsuarioEntity usuarioEntity = UsuarioMapper.paraEntidade(usuarioExistente);
       UsuarioEntity salvo = usuarioRepository.save(usuarioEntity);
       return UsuarioMapper.paraDominioDeOptional(salvo);
    }
}
