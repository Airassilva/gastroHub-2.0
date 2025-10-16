package pos.tech.cleanarchandjpa.infra.gateway;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.gateway.EnderecoGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.EnderecoMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.EnderecoRepository;

import java.util.UUID;

@Repository
@Transactional
public class EnderecoGatewayRepository implements EnderecoGateway {

    private final EnderecoRepository enderecoRepository;

    public EnderecoGatewayRepository(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Override
    public Endereco buscarEndereco(UUID idEndereco) {
       return enderecoRepository.findById(idEndereco)
               .map(EnderecoMapper::paraDominio)
               .orElse(null);
    }

    @Override
    public Endereco salvar(Endereco enderecoEncontrado) {
        var entidade = EnderecoMapper.paraEntidade(enderecoEncontrado);
        var enderecoSalvo =  enderecoRepository.save(entidade);
        return EnderecoMapper.paraDominio(enderecoSalvo);
    }
}
