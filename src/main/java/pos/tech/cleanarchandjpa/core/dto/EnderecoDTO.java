package pos.tech.cleanarchandjpa.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO{
      private String bairro;
      private String cep;
      private String cidade;
      private String estado;
      private String rua;
      private String complemento;
      private String numero;
}
