package pos.tech.cleanarchandjpa.core.dto.restaurante;

import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;

import java.util.List;

public record RestauranteUpdateDTO(
        List<HorarioFuncionamentoDTO> horarioFuncionamentoDTO,
        String tipoDeCozinha,
        EnderecoDTO enderecoDTO
) {
}
