package pos.tech.cleanarchandjpa.infra.dto;

import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;

public record UsuarioRequestDTO(
        String nome,

        String email,

        String telefone,

        String cpf,

        String cnpj,

        String login,

        String senha,

        TipoUsuario tipoUsuario,

        Endereco endereco) {
}
