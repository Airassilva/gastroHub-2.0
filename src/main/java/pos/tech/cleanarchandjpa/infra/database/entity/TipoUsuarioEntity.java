package pos.tech.cleanarchandjpa.infra.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @NotBlank(message = "O campo informando o tipo do usuário é obrigatório.")
    @Column(unique = true)
    private String tipoUsuario;

    @OneToMany(mappedBy = "tipoUsuario", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JsonIgnore
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    private boolean ativo;

    public TipoUsuarioEntity(UUID id, String tipoUsuario, List<UsuarioEntity> usuarios) {
        this.id = id;
        this.tipoUsuario = tipoUsuario;
        this.usuarios = usuarios;
        ativo = true;
    }
}
