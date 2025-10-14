package pos.tech.cleanarchandjpa.infra.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurante")
@Getter
@Setter
@NoArgsConstructor
public class RestauranteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O campo tipo de cozinha é obrigatório.")
    private String tipoDeCozinha;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private EnderecoEntity endereco;

    @ManyToOne
    @JoinColumn(name = "dono_id")
    private UsuarioEntity dono;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "restaurante_id")
    private List<HorarioFuncionamentoEntity> horarios = new ArrayList<>();

    public boolean estaAberto() {
        LocalDateTime agora = LocalDateTime.now();
        DayOfWeek diaAtual = agora.getDayOfWeek();
        LocalTime horaAtual = agora.toLocalTime();

        return horarios.stream()
                .filter(h -> h.getDiaSemana() == diaAtual)
                .anyMatch(h -> h.estaAberto(horaAtual));
    }
}
