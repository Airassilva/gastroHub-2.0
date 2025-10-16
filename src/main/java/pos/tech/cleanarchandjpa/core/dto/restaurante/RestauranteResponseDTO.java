package pos.tech.cleanarchandjpa.core.dto.restaurante;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RestauranteResponseDTO {
    private UUID id;
    private String nomeRestaurante;
    private UUID idUsuarioDono;

    public RestauranteResponseDTO(UUID id, String nome, UUID idUsuarioDono) {
        this.id = id;
        this.nomeRestaurante = nome;
        this.idUsuarioDono = idUsuarioDono;
    }
}
