package pos.tech.cleanarchandjpa.core.dto.cardapio;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CardapioResponseDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private double preco;

    public CardapioResponseDTO(UUID id, String nome, String descricao, double preco) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }
}
