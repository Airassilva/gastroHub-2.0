package pos.tech.cleanarchandjpa.core.dto.usuario;

import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;

public record UsuarioUpdateDTO(
        String email,
        EnderecoDTO enderecoDTO ,
        String login,
        String senha,
        String telefone
) {
}
