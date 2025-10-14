package pos.tech.cleanarchandjpa.infra.database.mapper;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioUpdateDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.TipoUsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;

import java.util.Collections;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TipoUsuarioMapper {

    public static TipoUsuario paraDominioDeDto(@Valid TipoUsuarioRequestDTO requestDTO) {
        return new TipoUsuario(
                null,
                requestDTO.tipoUsuario(),
                null
        );
    }

    public static TipoUsuarioEntity paraEntidade(TipoUsuario tipoUsuario) {
        return new TipoUsuarioEntity(
                tipoUsuario.getId(),
                tipoUsuario.getNomeTipoUsuario(),
                Collections.singletonList(UsuarioMapper.paraEntidade((Usuario) tipoUsuario.getUsuario()))
        );
    }

    public static TipoUsuario paraDominio(TipoUsuarioEntity tipoUsuarioSalvo) {
        return new TipoUsuario(
                tipoUsuarioSalvo.getId(),
                tipoUsuarioSalvo.getTipoUsuario(),
                Collections.singletonList(UsuarioMapper.paraDominio((UsuarioEntity) tipoUsuarioSalvo.getUsuarios()))
        );
    }

    public static TipoUsuarioResponseDTO paraResponseDeDominio(TipoUsuario tipoUsuarioCriado) {
        return new TipoUsuarioResponseDTO(
                tipoUsuarioCriado.getId(),
                tipoUsuarioCriado.getNomeTipoUsuario(),
                tipoUsuarioCriado.getUsuario()
        );
    }

    public static TipoUsuario paraDominioDeDtoUpdate(UUID id, @Valid TipoUsuarioUpdateDTO updateDTO) {
        return new TipoUsuario(
                id,
                updateDTO.getTipoUsuario()
        );
    }
}
