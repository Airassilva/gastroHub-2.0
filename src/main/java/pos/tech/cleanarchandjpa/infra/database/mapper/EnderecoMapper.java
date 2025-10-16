package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnderecoMapper {

    public static Endereco paraDominio(EnderecoEntity enderecoEntity) {
        if (enderecoEntity == null) {
            return null;
        }
        return new Endereco(
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

    public static Endereco deDtoParaDominio(EnderecoDTO enderecoDTO) {
        if (enderecoDTO == null)
            return null;

        return new Endereco(
                null,
                enderecoDTO.rua(),
                enderecoDTO.numero(),
                enderecoDTO.cidade(),
                enderecoDTO.estado(),
                enderecoDTO.cep(),
                enderecoDTO.bairro(),
                enderecoDTO.complemento()
        );
    }

    public static EnderecoEntity paraEntidade(Endereco endereco) {
        var usuario = UsuarioMapper.paraEntidade((Usuario) endereco.getUsuarios());
        List<UsuarioEntity> usuarios = new ArrayList<>();
        usuarios.add(usuario);
        return new EnderecoEntity(
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.getBairro(),
                endereco.getComplemento(),
                usuarios
        );
    }
}
