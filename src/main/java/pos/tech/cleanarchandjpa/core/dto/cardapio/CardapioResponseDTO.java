package pos.tech.cleanarchandjpa.core.dto.cardapio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardapioResponseDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private double preco;
}
