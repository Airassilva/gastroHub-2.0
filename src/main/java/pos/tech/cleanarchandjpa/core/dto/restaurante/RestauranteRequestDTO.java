package pos.tech.cleanarchandjpa.core.dto.restaurante;


import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;

import java.util.List;
import java.util.UUID;

public record RestauranteRequestDTO(
        String nome,
        EnderecoDTO endereco,
        UUID usuarioIdDono,
        List<HorarioFuncionamentoDTO> horarios,
        String tipoDeCozinha) {
}
