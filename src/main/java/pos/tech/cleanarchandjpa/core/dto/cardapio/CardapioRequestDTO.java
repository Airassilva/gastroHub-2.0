package pos.tech.cleanarchandjpa.core.dto.cardapio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardapioRequestDTO {
    String nome;
    String descricao;
    double preco;
    String caminhoFoto;
}
