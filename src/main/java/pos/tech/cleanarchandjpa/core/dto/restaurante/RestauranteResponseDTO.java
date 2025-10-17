package pos.tech.cleanarchandjpa.core.dto.restaurante;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteResponseDTO {
    private UUID id;
    private String nomeRestaurante;
    private UUID idUsuarioDono;
}
