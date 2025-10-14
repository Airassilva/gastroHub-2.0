package pos.tech.cleanarchandjpa.core.gateway;

import pos.tech.cleanarchandjpa.core.domain.Endereco;

import java.util.UUID;

public interface EnderecoGateway {
    Endereco buscarEndereco(UUID idEndereco);
    Endereco salvar(Endereco enderecoEncontrado);
}
