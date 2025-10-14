package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.TipoUsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioUpdateDTO;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioMapper {

    public static Usuario paraDominioDeDto(UsuarioRequestDTO dto) {
       var endereco =  EnderecoMapper.deDtoParaDominio(dto.enderecoDTO());
        return new Usuario(
               null,
                dto.nome(),
                dto.email(),
                dto.cpf(),
                dto.cnpj(),
                dto.telefone(),
                dto.login(),
                dto.senha(),
                endereco
                );
    }

    public static Usuario paraDominioDeDtoUpdate(UsuarioUpdateDTO dto) {
        var endereco =  EnderecoMapper.deDtoParaDominio(dto.enderecoDTO());
        return new Usuario(
                null,
                dto.email(),
                endereco,
                dto.login(),
                dto.senha()
        );
    }

    public static UsuarioResponseDTO paraResponseDeDomain(Usuario domain) {
        return new UsuarioResponseDTO(
                domain.getId(),
                domain.getNome(),
                domain.getEmail());
    }

    public static Optional<UsuarioResponseDTO> toResponseDtoOptional(Optional<Usuario> domain) {
        return domain.map(UsuarioMapper::paraResponseDeDomain);
    }

    public static UsuarioEntity paraEntidade(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail());
        entity.setCpf(usuario.getCpf());
        entity.setCnpj(usuario.getCnpj());
        entity.setTelefone(usuario.getTelefone());
        entity.setLogin(usuario.getLogin());
        entity.setSenha(usuario.getSenha());
        entity.setAtivo(usuario.isAtivo());
        entity.setDataCriacao(usuario.getDataCriacao() != null ? usuario.getDataCriacao() : new Date());
        entity.setDataUltimaAlteracao(new Date());

        // TipoUsuario para TipoUsuarioEntity
        if (usuario.getTipoUsuario() != null) {
            TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity();
            tipoEntity.setId(usuario.getTipoUsuario().getId());
            tipoEntity.setTipoUsuario(usuario.getTipoUsuario().getNomeTipoUsuario());
            entity.setTipoUsuarioEntity(tipoEntity);
        }

        // Endereco para EnderecoEntity
        if (usuario.getEndereco() != null) {
            Endereco endereco = usuario.getEndereco();
            EnderecoEntity enderecoEntity = new EnderecoEntity();
            enderecoEntity.setId(endereco.getId());
            enderecoEntity.setRua(endereco.getRua());
            enderecoEntity.setNumero(endereco.getNumero());
            enderecoEntity.setCidade(endereco.getCidade());
            enderecoEntity.setEstado(endereco.getEstado());
            enderecoEntity.setCep(endereco.getCep());
            entity.setEndereco(List.of(enderecoEntity));
        } else {
            entity.setEndereco(Collections.emptyList());
        }

        return entity;
    }


    public static Usuario paraDominio(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        Endereco enderecoDomain = null;

        if (entity.getEndereco() != null && !entity.getEndereco().isEmpty()) {
            var enderecoEntity = entity.getEndereco().stream().findFirst().orElse(null);
            if (enderecoEntity != null) {
                enderecoDomain = new Endereco(
                        enderecoEntity.getId(),
                        enderecoEntity.getRua(),
                        enderecoEntity.getNumero(),
                        enderecoEntity.getCidade(),
                        enderecoEntity.getEstado(),
                        enderecoEntity.getCep()
                );
            }
        }

        TipoUsuario tipoUsuarioDomain = null;
        if (entity.getTipoUsuarioEntity() != null) {
            tipoUsuarioDomain = new TipoUsuario(
                    entity.getTipoUsuarioEntity().getId(),
                    entity.getTipoUsuarioEntity().getTipoUsuario()
            );
        }

        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getCnpj(),
                entity.getTelefone(),
                entity.isAtivo(),
                entity.getDataCriacao(),
                entity.getDataUltimaAlteracao(),
                entity.getLogin(),
                entity.getSenha(),
                tipoUsuarioDomain,
                enderecoDomain
        );
    }

    public static Optional<Usuario> paraDominioOptional(UsuarioEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(paraDominio(entity));
    }
}
