package pos.tech.cleanarchandjpa.infra.dto;

import pos.tech.cleanarchandjpa.core.domain.Endereco;

import java.util.UUID;

public record UsuarioUpdateDTO(
        UUID id,
        String email,
        Endereco endereco
) {
}
