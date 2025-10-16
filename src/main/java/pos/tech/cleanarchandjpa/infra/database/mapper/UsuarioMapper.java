package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;
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
                dto.cpf(),
                dto.cnpj(),
                dto.email(),
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

    public static UsuarioEntity paraEntidade(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNome(usuario.getNome());
        entity.setCpf(usuario.getCpf());
        entity.setCnpj(usuario.getCnpj());
        entity.setEmail(usuario.getEmail());
        entity.setTelefone(usuario.getTelefone());
        entity.setLogin(usuario.getLogin());
        entity.setSenha(usuario.getSenha());
        if (usuario.getTipoUsuario() != null) {
            var tipoUsuarioEntity = TipoUsuarioMapper.paraEntidade(usuario.getTipoUsuario());
            entity.setTipoUsuario(tipoUsuarioEntity);
        }
        entity.setAtivo(true);
        entity.setDataCriacao(usuario.getDataCriacao() != null ? usuario.getDataCriacao() : new Date());
        entity.setDataUltimaAlteracao(new Date());

        List<UsuarioEntity> usuarios = new ArrayList<>();
        usuarios.add(entity);

        if (usuario.getEndereco() != null) {
            Endereco endereco = usuario.getEndereco();
            EnderecoEntity enderecoEntity = new EnderecoEntity();
                enderecoEntity.setId(endereco.getId());
                enderecoEntity.setRua(endereco.getRua());
                enderecoEntity.setBairro(endereco.getBairro());
                enderecoEntity.setCidade(endereco.getCidade());
                enderecoEntity.setEstado(endereco.getEstado());
                enderecoEntity.setCep(endereco.getCep());
                enderecoEntity.setNumero(endereco.getNumero());
                enderecoEntity.setComplemento(endereco.getComplemento());
                enderecoEntity.setDataUltimaALteracao(endereco.getDataUltimaAlteracao());
                enderecoEntity.setUsuarios(usuarios);
                entity.setEndereco(List.of(enderecoEntity));
        } else {
            entity.setEndereco(Collections.emptyList());
        }
        return entity;
    }

    public static Usuario paraDominioBasico(UsuarioEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getLogin(),
                entity.getCpf(),
                entity.getCnpj()
        );
    }

    public static Usuario paraDominioDeOptional(UsuarioEntity entity) {
        if (entity == null) return null;

        Endereco enderecoDomain = null;
        if (entity.getEndereco() != null && !entity.getEndereco().isEmpty()) {
            var enderecoEntity = entity.getEndereco().stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            if (enderecoEntity != null) {
                enderecoDomain = new Endereco(
                        enderecoEntity.getId(),
                        enderecoEntity.getRua(),
                        enderecoEntity.getNumero(),
                        enderecoEntity.getCidade(),
                        enderecoEntity.getEstado(),
                        enderecoEntity.getCep(),
                        enderecoEntity.getBairro(),
                        enderecoEntity.getComplemento()
                );
            }
        }

        TipoUsuario tipoUsuarioDomain = entity.getTipoUsuario() != null
                ? TipoUsuarioMapper.paraDominio(entity.getTipoUsuario())
                : null;

        if (tipoUsuarioDomain != null) {
            return new Usuario(
                    entity.getId(),
                    entity.getEmail(),
                    enderecoDomain,
                    entity.getLogin(),
                    entity.getSenha(),
                    tipoUsuarioDomain,
                    entity.getCpf(),
                    entity.getCnpj(),
                    entity.getTelefone()
            );
        } else {
            return new Usuario(
                    entity.getId(),
                    entity.getNome(),
                    entity.getCpf(),
                    entity.getCnpj(),
                    entity.getEmail(),
                    entity.getTelefone(),
                    entity.getLogin(),
                    entity.getSenha(),
                    enderecoDomain
            );
        }
    }


    public static Usuario paraDominio(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        Endereco enderecoDomain = null;

        if (entity.getEndereco() != null && !entity.getEndereco().isEmpty()) {
            var enderecoEntity = entity.getEndereco().stream().findFirst().orElse(null);
            enderecoDomain = new Endereco(
                    enderecoEntity.getId(),
                    enderecoEntity.getRua(),
                    enderecoEntity.getNumero(),
                    enderecoEntity.getCidade(),
                    enderecoEntity.getEstado(),
                    enderecoEntity.getCep(),
                    enderecoEntity.getBairro(),
                    enderecoEntity.getComplemento()
            );
        }

        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getCnpj(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getLogin(),
                entity.getSenha(),
                enderecoDomain
        );
    }
}
