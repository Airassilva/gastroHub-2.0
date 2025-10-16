package pos.tech.cleanarchandjpa.infra.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "horario_funcionamento")
@Getter
@Setter
@NoArgsConstructor
public class HorarioFuncionamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    private String diaSemana;

    private LocalTime abertura;
    private LocalTime fechamento;
    private boolean fechado;

    public HorarioFuncionamentoEntity(String diaSemana, LocalTime abertura, LocalTime fechamento) {
        this.diaSemana = diaSemana;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public boolean estaAberto(LocalTime agora) {
        if (fechado) return false;
        return !agora.isBefore(abertura) && agora.isBefore(fechamento);
    }
}
