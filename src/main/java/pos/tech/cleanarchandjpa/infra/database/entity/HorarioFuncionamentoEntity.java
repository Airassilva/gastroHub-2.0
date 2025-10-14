package pos.tech.cleanarchandjpa.infra.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
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
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek diaSemana;

    private LocalTime abertura;
    private LocalTime fechamento;
    private boolean fechado;

    public HorarioFuncionamentoEntity(DayOfWeek dayOfWeek, LocalTime abertura, LocalTime fechamento) {
        this.diaSemana = dayOfWeek;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public boolean estaAberto(LocalTime agora) {
        if (fechado) return false;
        return !agora.isBefore(abertura) && agora.isBefore(fechamento);
    }
}
