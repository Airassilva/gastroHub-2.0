package pos.tech.cleanarchandjpa.infra.gateway;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.exception.TipoUsuarioNaoEncontradoException;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.TipoUsuarioMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.TipoUsuarioRepository;

import java.util.UUID;

@Repository
@Transactional
public class TipoUsuarioRepositoryGateway implements TipoUsuarioGateway {
    private final TipoUsuarioRepository tipoUsuarioRepository;

    public TipoUsuarioRepositoryGateway(TipoUsuarioRepository tipoUsuarioRepository) {
        this.tipoUsuarioRepository = tipoUsuarioRepository;
    }

    @Override
    public TipoUsuario salvar(TipoUsuario tipoUsuario) {
       var entidade = TipoUsuarioMapper.paraEntidade(tipoUsuario);
       var tipoUsuarioSalvo = tipoUsuarioRepository.save(entidade);
       return TipoUsuarioMapper.paraDominio(tipoUsuarioSalvo);
    }

    @Override
    public PaginacaoResult<TipoUsuario> listarTiposDeUsuarios(ParametrosPag parametrosPag) {
        var paginacao = PaginacaoMapper.deParametrosPagParaPageable(parametrosPag);
        var page = tipoUsuarioRepository.findAll(paginacao);
        return PaginacaoMapper.dePageParaPaginacaoTipoUsuario(page);
    }

    @Override
    public TipoUsuario buscarTipoUsuario(UUID id) {
       var entidade = tipoUsuarioRepository.findByUsuariosId(id);
       return TipoUsuarioMapper.paraDominio(entidade);
    }

    @Override
    public void deletarUsuario(UUID id) {
        tipoUsuarioRepository.deleteById(id);
    }

    @Override
    public TipoUsuario buscarPeloId(UUID id) {
      return tipoUsuarioRepository.findById(id)
              .map(TipoUsuarioMapper::paraDominio)
              .orElse(null);
    }
}
