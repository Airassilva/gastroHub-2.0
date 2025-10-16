package pos.tech.cleanarchandjpa.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pos.tech.cleanarchandjpa.core.gateway.RestauranteGateway;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.core.usecase.AtualizarEnderecoUseCase;
import pos.tech.cleanarchandjpa.core.usecase.restaurante.*;
import pos.tech.cleanarchandjpa.infra.database.repository.RestauranteRepository;
import pos.tech.cleanarchandjpa.infra.gateway.RestauranteGatewayRepository;

@Configuration
public class RestauranteConfig {

    @Bean
    public RestauranteGateway restauranteGateway(RestauranteRepository restauranteRepository) {
        return new RestauranteGatewayRepository(restauranteRepository);
    }

    @Bean
    public CriarRestauranteUsecase criarRestauranteUsecase(RestauranteGateway restauranteGateway, UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        return new CriarRestauranteUsecase(restauranteGateway, usuarioGateway, tipoUsuarioGateway);
    }

    @Bean
    public ListarRestauranteUseCase listarRestauranteUsecase(RestauranteGateway restauranteGateway) {
        return new ListarRestauranteUseCase(restauranteGateway);
    }

    @Bean
    public AtualizarRestauranteUseCase atualizarResturanteUseCase(RestauranteGateway restauranteGateway) {
        return new AtualizarRestauranteUseCase(restauranteGateway);
    }

    @Bean
    public DeletarRestauranteUseCase deletarRestauranteUseCase(RestauranteGateway restauranteGateway) {
        return new DeletarRestauranteUseCase(restauranteGateway);
    }

    @Bean
    public AtualizarRestauranteComEnderecoUseCase atualizarRestauranteComEnderecoUseCase(
            AtualizarRestauranteUseCase atualizarRestauranteUseCase,
            AtualizarEnderecoUseCase atualizarEnderecoUseCase) {
        return new AtualizarRestauranteComEnderecoUseCaseImpl(
                atualizarRestauranteUseCase,
                atualizarEnderecoUseCase
        );
    }
}
