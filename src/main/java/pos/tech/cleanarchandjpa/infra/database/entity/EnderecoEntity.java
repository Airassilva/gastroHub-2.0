package pos.tech.cleanarchandjpa.infra.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "endereco")
@NoArgsConstructor
public class EnderecoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
