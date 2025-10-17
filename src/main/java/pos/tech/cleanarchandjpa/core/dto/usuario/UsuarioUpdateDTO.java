package pos.tech.cleanarchandjpa.core.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
      private String email;
      private EnderecoDTO enderecoDTO ;
      private String login;
      private String senha;
      private String telefone;
}
