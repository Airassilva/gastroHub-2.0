package pos.tech.cleanarchandjpa.core.dto.restaurante;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteUpdateDTO{
       private List<HorarioFuncionamentoDTO> horarioFuncionamentoDTO;
       private String tipoDeCozinha;
       private EnderecoDTO enderecoDTO;
}
