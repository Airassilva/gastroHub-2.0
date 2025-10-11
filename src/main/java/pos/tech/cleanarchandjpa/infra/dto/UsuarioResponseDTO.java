package pos.tech.cleanarchandjpa.infra.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsuarioResponseDTO{
    UUID id;
    String nome;
    String email;

    public UsuarioResponseDTO(UUID id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
}
