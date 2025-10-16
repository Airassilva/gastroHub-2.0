package pos.tech.cleanarchandjpa.infra.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "endereco")
@NoArgsConstructor
public class EnderecoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @NotBlank(message = "O campo rua é obrigatório.")
    private String rua;

    @NotBlank(message = "O campo bairro é obrigatório.")
    private String bairro;

    @NotBlank(message = "O campo cidade é obrigatório.")
    private String cidade;

    @NotBlank(message = "O campo estado é obrigatório.")
    private String estado;

    @NotBlank(message = "O campo cep é obrigatório.")
    private String cep;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaALteracao;

    private String numero;

    private String complemento;

    @ManyToMany(mappedBy = "endereco")
    @JsonIgnore
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    public EnderecoEntity(UUID id, String rua, String bairro, String cidade, String estado, String cep, String numero, String complemento, List<UsuarioEntity> usuarios) {
        this.id = id;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.bairro = bairro;
        this.complemento = complemento;
        this.usuarios = usuarios;
    }
}
