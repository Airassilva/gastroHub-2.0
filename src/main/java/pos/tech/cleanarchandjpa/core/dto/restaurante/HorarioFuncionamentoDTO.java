package pos.tech.cleanarchandjpa.core.dto.restaurante;

public record HorarioFuncionamentoDTO(
        String diaSemana,
        String abertura,
        String fechamento,
        boolean fechado
) {}