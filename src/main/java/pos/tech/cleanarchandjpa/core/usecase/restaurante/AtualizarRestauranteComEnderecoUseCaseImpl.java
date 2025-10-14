package pos.tech.cleanarchandjpa.core.usecase.restaurante;

import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.usecase.AtualizarEnderecoUseCase;

import java.util.UUID;

public class AtualizarRestauranteComEnderecoUseCaseImpl implements AtualizarRestauranteComEnderecoUseCase {
    private final AtualizarRestauranteUseCase atualizarResturanteUseCase;
    private final AtualizarEnderecoUseCase atualizarEnderecoUseCase;

    public AtualizarRestauranteComEnderecoUseCaseImpl(
            AtualizarRestauranteUseCase atualizarRestauranteUseCase,
            AtualizarEnderecoUseCase atualizarEnderecoUseCase) {
        this.atualizarResturanteUseCase = atualizarRestauranteUseCase;
        this.atualizarEnderecoUseCase = atualizarEnderecoUseCase;
    }

    @Override
    public Restaurante atualizarRestauranteComEndereco(Restaurante restaurante, UUID id) {
        var restauranteAtualizado = atualizarResturanteUseCase.atualizarRestaurante(restaurante, id);
        atualizarEnderecoUseCase.atualizarEnderecoSeNecessario(restaurante, restauranteAtualizado);
        return restauranteAtualizado;
    }
}