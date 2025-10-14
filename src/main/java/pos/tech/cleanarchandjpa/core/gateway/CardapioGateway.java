package pos.tech.cleanarchandjpa.core.gateway;

import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;

import java.util.UUID;

public interface CardapioGateway {
    Cardapio salvarCardapio(Cardapio cardapio);
    PaginacaoResult<Cardapio> listarCardapios(Restaurante restaurante, ParametrosPag parametrosPag);

    Cardapio buscarCardapioPeloId(UUID id);

    void deletarCardapioPeloId(UUID id);
}
