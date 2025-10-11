package pos.tech.cleanarchandjpa.infra.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O campo email é obrigatório.")
    @Email
    private String email;

    @CPF
    private String cpf;

    @CNPJ
    private String cnpj;

    @NotBlank(message = "O campo de telefone é obrigatório.")
    private String telefone;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaAlteracao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @NotBlank(message = "O campo login é obrigatório.")
    private String login;

    @NotBlank(message = "O campo senha é obrigatório.")
    private String senha;

    @ManyToOne
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    private TipoUsuarioEntity tipoUsuarioEntity;

    @OneToMany(mappedBy = "dono")
    private List<RestauranteEntity> restaurantesComoDono = new ArrayList<>();

    @ManyToMany(mappedBy = "clientes")
    private List<RestauranteEntity> restaurantesComoCliente = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "usuario_enderecos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "endereco_id")
    )
    private List<EnderecoEntity> endereco = new ArrayList<>();
}
