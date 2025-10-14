package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.dto.EnderecoDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnderecoMapper {

    public static Endereco toDomain(EnderecoEntity enderecoEntity) {
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

    public static Endereco toDomainDto(EnderecoDTO enderecoDTO) {
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

    public static EnderecoEntity toEntity(Endereco enderecoEncontrado) {
        if (enderecoEncontrado == null) return null;
        return new EnderecoEntity(
                enderecoEncontrado.getId(),
                enderecoEncontrado.getRua(),
                enderecoEncontrado.getBairro(),
                enderecoEncontrado.getCidade(),
                enderecoEncontrado.getEstado(),
                enderecoEncontrado.getCep(),
                enderecoEncontrado.getNumero()
        );
    }
}
