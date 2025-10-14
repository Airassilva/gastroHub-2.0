package pos.tech.cleanarchandjpa.core.usecase.cardapio;

import pos.tech.cleanarchandjpa.core.gateway.CardapioGateway;

import java.util.UUID;

public class DeletarCardapioUseCase {
    private final CardapioGateway cardapioGateway;

    public DeletarCardapioUseCase(CardapioGateway cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    public void deletarCardapio(UUID id) {
        cardapioGateway.deletarCardapioPeloId(id);
    }
}
