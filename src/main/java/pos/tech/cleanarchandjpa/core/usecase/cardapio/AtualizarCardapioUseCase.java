package pos.tech.cleanarchandjpa.core.usecase.cardapio;

import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.gateway.CardapioGateway;

import java.util.UUID;

public class AtualizarCardapioUseCase {

    private final CardapioGateway cardapioGateway;

    public AtualizarCardapioUseCase(CardapioGateway cardapioGateway) {
        this.cardapioGateway = cardapioGateway;
    }

    public Cardapio atualizarCardapio(Cardapio dominio, UUID id) {
        var cardapio = cardapioGateway.buscarCardapioPeloId(id);
        cardapio.comNovosDados(dominio);
        return cardapioGateway.salvarCardapio(cardapio);
    }
}
