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
public class UsuarioRequestDTO {
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String cnpj;
    private String login;
    private String senha;
    private EnderecoDTO enderecoDTO;
}