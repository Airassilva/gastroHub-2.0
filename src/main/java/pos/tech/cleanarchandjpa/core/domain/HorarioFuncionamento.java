package pos.tech.cleanarchandjpa.core.domain;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
public class HorarioFuncionamento {
    private DayOfWeek diaSemana;
    private LocalTime abertura;
    private LocalTime fechamento;
    private boolean fechado;

    public HorarioFuncionamento(DayOfWeek dayOfWeek, LocalTime parse, LocalTime parse1) {
        this.diaSemana = dayOfWeek;
        this.abertura = parse;
        this.fechamento = parse1;
    }

    public boolean estaAberto(LocalTime agora) {
        if (fechado) return false;
        return !agora.isBefore(abertura) && agora.isBefore(fechamento);
    }
}
