package pos.tech.cleanarchandjpa.infra.gateway;

import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.TipoUsuarioMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.TipoUsuarioRepository;

import java.util.Optional;
import java.util.UUID;

public class TipoUsuarioRepositoryGateway implements TipoUsuarioGateway {
    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioRepositoryGateway(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
       var entidade = TipoUsuarioMapper.paraEntidade(tipoUsuario);
       var tipoUsuarioSalvo = tipoUsuarioRepository.save(entidade);
       return TipoUsuarioMapper.paraDominioOtional(Optional.of(tipoUsuarioSalvo));
    }

    @Override
    public PaginacaoResult<TipoUsuario> listarTiposDeUsuarios(Usuario usuario, ParametrosPag parametrosPag) {
        var paginacao = PaginacaoMapper.deParametrosPagParaPageable(parametrosPag);
        var page = tipoUsuarioRepository.findAllByUsuarioId(usuario.getId(), paginacao);
        return PaginacaoMapper.dePageParaPaginacaoTipoUsuario(page);
    }

    @Override
    public TipoUsuario buscarTipoUsuario(UUID id) {
        var tipoUsuario = tipoUsuarioRepository.findById(id);
        return TipoUsuarioMapper.paraDominioOtional(tipoUsuario);
    }

    @Override
    public void deletarUsuario(UUID id) {
        tipoUsuarioRepository.deleteById(id);
    }
}
