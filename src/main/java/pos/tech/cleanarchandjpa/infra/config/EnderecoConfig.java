package pos.tech.cleanarchandjpa.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pos.tech.cleanarchandjpa.core.gateway.EnderecoGateway;
import pos.tech.cleanarchandjpa.core.usecase.AtualizarEnderecoUseCase;
import pos.tech.cleanarchandjpa.infra.database.repository.EnderecoRepository;
import pos.tech.cleanarchandjpa.infra.gateway.EnderecoGatewayRepository;

@Configuration
public class EnderecoConfig {
    @Bean
    public EnderecoGateway enderecoGateway(EnderecoRepository enderecoRepository) {
        return new EnderecoGatewayRepository(enderecoRepository);
    }

    @Bean
    public AtualizarEnderecoUseCase atualizarEnderecoUseCase(EnderecoGateway enderecoGateway) {
        return new AtualizarEnderecoUseCase(enderecoGateway);
    }
}
