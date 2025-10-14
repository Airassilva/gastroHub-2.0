package pos.tech.cleanarchandjpa.core.dto.restaurante;

import java.time.DayOfWeek;

public record HorarioFuncionamentoDTO(
        DayOfWeek diaSemana,
        String abertura,
        String fechamento,
        boolean fechado
) {}