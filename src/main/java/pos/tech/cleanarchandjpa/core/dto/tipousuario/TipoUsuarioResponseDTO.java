package pos.tech.cleanarchandjpa.core.dto.tipousuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoUsuarioResponseDTO {
    private UUID id;
    private String tipoUsuario;
    private List<UUID> idsUsuarios;
}

