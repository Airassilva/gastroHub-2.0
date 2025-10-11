package pos.tech.cleanarchandjpa.infra.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotBlank(message = "O campo informando o tipo do usuário é obrigatório.")
    @Column(unique = true)
    private String tipoUsuario;

    @OneToMany(mappedBy = "tipoUsuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    public TipoUsuarioEntity(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario.toString();
    }

    public TipoUsuarioEntity() {
    }
}
