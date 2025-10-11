package pos.tech.cleanarchandjpa.infra.gateway;

import pos.tech.cleanarchandjpa.core.gateway.EnderecoGateway;
import pos.tech.cleanarchandjpa.infra.database.repository.EnderecoRepository;

public class EnderecoRepositoryGateway implements EnderecoGateway {
    private final EnderecoRepository enderecoRepository;

    public EnderecoRepositoryGateway(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }
}
