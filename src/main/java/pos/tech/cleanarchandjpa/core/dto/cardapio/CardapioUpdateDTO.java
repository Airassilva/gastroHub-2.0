package pos.tech.cleanarchandjpa.core.dto.cardapio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardapioUpdateDTO {
       private String nome;
       private double preco;
       private String descricao;
       private String caminhoFoto;
}
