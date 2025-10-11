package pos.tech.cleanarchandjpa.core.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class TipoUsuario {
   private UUID id;
   private String nomeTipoUsuario;

    public TipoUsuario(UUID id, String tipoUsuario) {
        this.id = id;
        this.nomeTipoUsuario = tipoUsuario;
    }
}
