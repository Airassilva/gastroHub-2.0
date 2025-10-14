package pos.tech.cleanarchandjpa.core.dto.cardapio;

import java.util.UUID;

public class CardapioResponseDTO {
    UUID id;
    String nome;
    String descricao;

    public CardapioResponseDTO(UUID id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
}
