package pos.tech.cleanarchandjpa.infra.gateway;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.gateway.CardapioGateway;
import pos.tech.cleanarchandjpa.infra.database.mapper.CardapioMapper;
import pos.tech.cleanarchandjpa.infra.database.mapper.PaginacaoMapper;
import pos.tech.cleanarchandjpa.infra.database.repository.CardapioRepository;

import java.util.UUID;

@Repository
@Transactional
public class CardapioGatewayRepository implements CardapioGateway {
    private final CardapioRepository cardapioRepository;

    public CardapioGatewayRepository(CardapioRepository cardapioRepository) {
        this.cardapioRepository = cardapioRepository;
    }

    @Override
    public Cardapio salvarCardapio(Cardapio cardapio) {
        var entidade = CardapioMapper.paraEntidade(cardapio);
        var cardapioSalvo = cardapioRepository.save(entidade);
        return CardapioMapper.paraDominioDeEntidadeOptional(cardapioSalvo);
    }

    @Override
    public PaginacaoResult<Cardapio> listarCardapios(Restaurante restaurante, ParametrosPag parametrosPag) {
        var paginacao = PaginacaoMapper.deParametrosPagParaPageable(parametrosPag);
        var page = cardapioRepository.findAllByRestauranteId(restaurante.getId(), paginacao);
        return PaginacaoMapper.dePageParaPaginacaoCardapio(page);
    }

    @Override
    public Cardapio buscarCardapioPeloId(UUID id) {
        return cardapioRepository.findById(id)
                .map(CardapioMapper::paraDominioDeEntidade)
                .orElse(null);
    }

    @Override
    public void deletarCardapioPeloId(UUID id) {
        cardapioRepository.deleteById(id);
    }
}
