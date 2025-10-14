package pos.tech.cleanarchandjpa.core.usecase.restaurante;

import pos.tech.cleanarchandjpa.core.domain.Restaurante;

import java.util.UUID;

public interface AtualizarRestauranteComEnderecoUseCase {
    Restaurante atualizarRestauranteComEndereco(Restaurante restaurante, UUID id);
}
