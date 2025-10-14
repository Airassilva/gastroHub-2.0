package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnderecoMapper {

    public static Endereco paraDominio(EnderecoEntity enderecoEntity) {
        if (enderecoEntity == null) return null;

        return new Endereco(
                enderecoEntity.getId(),
                enderecoEntity.getRua(),
                enderecoEntity.getNumero(),
                enderecoEntity.getCidade(),
                enderecoEntity.getEstado(),
                enderecoEntity.getCep()
        );
    }

    public static Endereco deDtoParaDominio(EnderecoDTO enderecoDTO) {
        if (enderecoDTO == null)
            return null;

        return new Endereco(
                null,
                enderecoDTO.cep(),
                enderecoDTO.rua(),
                enderecoDTO.numero(),
                enderecoDTO.cidade(),
                enderecoDTO.estado()
        );
    }

    public static EnderecoEntity paraEntidade(Endereco endereco) {
        if (endereco == null) return null;
        return new EnderecoEntity(
                endereco.getId(),
                endereco.getRua(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep(),
                endereco.getNumero()
        );
    }
}
