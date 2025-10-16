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

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TipoUsuarioMapper {

    public static TipoUsuario paraDominioDeDto(@Valid TipoUsuarioRequestDTO requestDTO) {
        return new TipoUsuario(
                null,
                requestDTO.tipoUsuario(),
                new ArrayList<>()
        );
    }

    public static TipoUsuarioEntity paraEntidade(TipoUsuario tipoUsuario) {
        return new TipoUsuarioEntity(
                tipoUsuario.getId(),
                tipoUsuario.getNomeTipoUsuario(),
                tipoUsuario.getUsuarios()
                        .stream()
                        .map(UsuarioMapper::paraEntidade)
                        .collect(Collectors.toList())
        );
    }

    public static TipoUsuario paraDominio(TipoUsuarioEntity tipoUsuarioSalvo) {
        return new TipoUsuario(
                tipoUsuarioSalvo.getId(),
                tipoUsuarioSalvo.getTipoUsuario(),
                tipoUsuarioSalvo.getUsuarios()
                        .stream()
                        .map(UsuarioMapper::paraDominioBasico)
                        .collect(Collectors.toList())
        );
    }

    public static TipoUsuarioResponseDTO paraResponseDeDominio(TipoUsuario tipoUsuarioCriado) {
        if (tipoUsuarioCriado == null) {
            return null;
        }

        List<UUID> idsUsuarios = tipoUsuarioCriado.getUsuarios() != null
                ? tipoUsuarioCriado.getUsuarios().stream()
                .filter(Objects::nonNull)
                .map(Usuario::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return new TipoUsuarioResponseDTO(
                tipoUsuarioCriado.getId(),
                tipoUsuarioCriado.getNomeTipoUsuario(),
                idsUsuarios
        );
    }

    public static TipoUsuario paraDominioDeDtoUpdate(UUID id, @Valid TipoUsuarioUpdateDTO updateDTO) {
        return new TipoUsuario(
                        id,
                        updateDTO.getTipoUsuario()
        );
    }
}
