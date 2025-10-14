package pos.tech.cleanarchandjpa.infra.gateway;

import jakarta.persistence.EntityNotFoundException;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.gateway.EnderecoGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.EnderecoMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.EnderecoRepository;

import java.util.UUID;

public class EnderecoGatewayRepository implements EnderecoGateway {

    private final EnderecoRepository enderecoRepository;

    public EnderecoGatewayRepository(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Override
    public Endereco buscarEndereco(UUID idEndereco) {
       var enderecoEncontrado = enderecoRepository.findById(idEndereco).orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
       return EnderecoMapper.paraDominio(enderecoEncontrado);
    }

    @Override
    public Endereco salvar(Endereco enderecoEncontrado) {
        var entidade = EnderecoMapper.paraEntidade(enderecoEncontrado);
        var enderecoSalvo =  enderecoRepository.save(entidade);
        return EnderecoMapper.paraDominio(enderecoSalvo);
    }
}
