package pos.tech.cleanarchandjpa.core.domain;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class HorarioFuncionamento {
    private String diaSemana;
    private LocalTime abertura;
    private LocalTime fechamento;
    private boolean fechado;

    public HorarioFuncionamento(String diaSemana, LocalTime parse, LocalTime parse1) {
        this.diaSemana = diaSemana;
        this.abertura = parse;
        this.fechamento = parse1;
    }

    public boolean estaAberto(LocalTime agora) {
        if (fechado) return false;
        return !agora.isBefore(abertura) && agora.isBefore(fechamento);
    }
}
