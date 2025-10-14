package pos.tech.cleanarchandjpa.core.dto.usuario;

import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;

public record UsuarioRequestDTO(
        String nome,

        String email,

        String telefone,

        String cpf,

        String cnpj,

        String login,

        String senha,

        EnderecoDTO enderecoDTO) {
}
