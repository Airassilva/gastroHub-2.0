package pos.tech.cleanarchandjpa.core.dto.tipousuario;

import java.util.List;
import java.util.UUID;

public class TipoUsuarioResponseDTO {
    private UUID id;
    private String tipoUsuario;
    private List<UUID> idsUsuarios;

    public TipoUsuarioResponseDTO(UUID id, String tipoUsuario, List<UUID> idsUsuarios) {
        this.id = id;
        this.tipoUsuario = tipoUsuario;
        this.idsUsuarios = idsUsuarios != null ? idsUsuarios : List.of();
    }

    public UUID getId() {
        return id;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public List<UUID> getIdsUsuarios() {
        return idsUsuarios;
    }
}

