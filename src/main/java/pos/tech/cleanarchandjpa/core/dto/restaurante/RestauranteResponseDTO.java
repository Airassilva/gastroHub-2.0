package pos.tech.cleanarchandjpa.core.dto.restaurante;

import java.util.UUID;

public class RestauranteResponseDTO {
    UUID id;
    String nomeRestaurante;
    UUID idUsuarioDono;

    public RestauranteResponseDTO(UUID id, String nome, UUID idUsuarioDono) {
        this.id = id;
        this.nomeRestaurante = nome;
        this.idUsuarioDono = idUsuarioDono;
    }
}
