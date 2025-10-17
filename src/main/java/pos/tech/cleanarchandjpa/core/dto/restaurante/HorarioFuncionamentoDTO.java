package pos.tech.cleanarchandjpa.core.dto.restaurante;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HorarioFuncionamentoDTO {
    private String diaSemana;
    private String abertura;
    private String fechamento;
    private boolean fechado;
}