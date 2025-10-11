package pos.tech.cleanarchandjpa.core.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UsuarioOutput {
    UUID id;
    String nome;
    String email;

    public UsuarioOutput(UUID id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
}
