package pos.tech.cleanarchandjpa.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pos.tech.cleanarchandjpa.core.gateway.CardapioGateway;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.AtualizarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.CriarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.DeletarCardapioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.cardapio.ListarCardapioPeloRestauranteUseCase;
import pos.tech.cleanarchandjpa.infra.database.repository.CardapioRepository;
import pos.tech.cleanarchandjpa.infra.gateway.CardapioGatewayRepository;

@Configuration
public class CardapioConfig {

    @Bean
    public CardapioGateway cardapioGateway(CardapioRepository cardapioRepository) {
        return new CardapioGatewayRepository(cardapioRepository);
    }

    @Bean
    public CriarCardapioUseCase criarCardapioUseCase(CardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
        return new CriarCardapioUseCase(cardapioGateway, restauranteGateway);
    }

    @Bean
    public ListarCardapioPeloRestauranteUseCase listarCardapioPeloRestauranteUseCase(CardapioGateway cardapioGateway, RestauranteGateway restauranteGateway) {
        return new ListarCardapioPeloRestauranteUseCase(cardapioGateway, restauranteGateway);
    }

    @Bean
    public AtualizarCardapioUseCase atualizarCardapioUseCase(CardapioGateway cardapioGateway) {
        return new AtualizarCardapioUseCase(cardapioGateway);
    }

    @Bean
    public DeletarCardapioUseCase deletarCardapioUseCase(CardapioGateway cardapioGateway) {
        return new DeletarCardapioUseCase(cardapioGateway);
    }
}