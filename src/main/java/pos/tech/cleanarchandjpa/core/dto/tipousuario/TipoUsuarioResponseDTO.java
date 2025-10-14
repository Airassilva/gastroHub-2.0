package pos.tech.cleanarchandjpa.core.dto.tipousuario;

import pos.tech.cleanarchandjpa.core.domain.Usuario;

import java.util.List;
import java.util.UUID;

public class TipoUsuarioResponseDTO {
    UUID id;
    String tipoUsuario;
    UUID idUsuario;

    public TipoUsuarioResponseDTO(UUID id, String nomeTipoUsuario, List<Usuario> usuario) {
        this.id = id;
        this.tipoUsuario = nomeTipoUsuario;
        this.idUsuario = usuario.getFirst().getId();
    }
}
