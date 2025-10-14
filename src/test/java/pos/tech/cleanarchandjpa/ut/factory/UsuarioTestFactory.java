package pos.tech.cleanarchandjpa.ut.factory;

import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.TipoUsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioResponseDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class UsuarioTestFactory {

    public static TipoUsuario criarTipoUsuarioDomain() {
        return new TipoUsuario(new UUID(0L, 0L), "dono");
    }

    public static TipoUsuarioEntity criarTipoUsuarioEntity() {
        TipoUsuarioEntity tipoEntity = new TipoUsuarioEntity();
        tipoEntity.setId(new UUID(0L, 0L));
        tipoEntity.setTipoUsuario("dono");
        return tipoEntity;
    }

    public static Endereco criarEnderecoDomain() {
        return new Endereco(new UUID(1L, 1L), "Rua das Flores", "123", "Recife", "PE", "50000-000");
    }

    public static EnderecoEntity criarEnderecoEntity() {
        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(new UUID(1L, 1L));
        enderecoEntity.setRua("Rua das Flores");
        enderecoEntity.setNumero("123");
        enderecoEntity.setCidade("Recife");
        enderecoEntity.setEstado("PE");
        enderecoEntity.setCep("50000-000");
        return enderecoEntity;
    }

    public static UsuarioRequestDTO criarUsuarioRequestDTO() {
        return new UsuarioRequestDTO(
                "Aira Soares",
                "aira@email.com",
                "(81) 99999-9999",
                "12345678900",
                null,
                "aira_login",
                "senhaSegura123",
                criarTipoUsuarioDomain(),
                criarEnderecoDomain()
        );
    }

    public static UsuarioResponseDTO criarUsuarioResponseDTO() {
        return new UsuarioResponseDTO(
                new UUID(1L,1L),
                "Aira Soares",
                "aira@email.com"
        );
    }

    public static Usuario criarUsuarioDomain() {
        return new Usuario(
                "Aira Soares",
                "aira@email.com",
                "12345678900",
                null,
                "(81) 99999-9999",
                "aira_login",
                "senhaSegura123",
                criarTipoUsuarioDomain(),
                criarEnderecoDomain()
        );
    }

    public static Usuario criarUsuarioDomainComId() {
        return new Usuario(
                new UUID(2L, 2L),
                "Aira Soares",
                "aira@email.com",
                "12345678900",
                null,
                "(81) 99999-9999",
                true,
                new Date(),
                new Date(),
                "aira_login",
                "senhaSegura123",
                criarTipoUsuarioDomain(),
                criarEnderecoDomain()
        );
    }

    public static UsuarioEntity criarUsuarioEntity() {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(new UUID(2L, 2L));
        entity.setNome("Aira Soares");
        entity.setEmail("aira@email.com");
        entity.setCpf("12345678900");
        entity.setCnpj(null);
        entity.setTelefone("(81) 99999-9999");
        entity.setAtivo(true);
        entity.setDataCriacao(new Date());
        entity.setDataUltimaAlteracao(new Date());
        entity.setLogin("aira_login");
        entity.setSenha("senhaSegura123");
        entity.setTipoUsuarioEntity(criarTipoUsuarioEntity());
        entity.setEndereco(new ArrayList<>());
        entity.getEndereco().add(criarEnderecoEntity());
        entity.setRestaurantesComoDono(new ArrayList<>());
        entity.setRestaurantesComoCliente(new ArrayList<>());
        return entity;
    }
}
