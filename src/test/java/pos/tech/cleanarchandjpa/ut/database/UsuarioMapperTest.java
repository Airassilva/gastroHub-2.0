package pos.tech.cleanarchandjpa.ut.database;

import org.junit.jupiter.api.Test;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.mapper.UsuarioMapper;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioRequestDTO;
import pos.tech.cleanarchandjpa.infra.dto.UsuarioResponseDTO;
import pos.tech.cleanarchandjpa.ut.factory.UsuarioTestFactory;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioMapperTest {

    @Test
    void deveConverterUsuarioRequestDTOParaDominio() {
        UsuarioRequestDTO requestDTO = UsuarioTestFactory.criarUsuarioRequestDTO();
        Usuario usuarioEsperado = UsuarioTestFactory.criarUsuarioDomain();

        Usuario resultado = UsuarioMapper.toDomainDto(requestDTO);

        assertThat(resultado)
                .isNotNull()
                .extracting("nome", "email", "cpf", "cnpj", "telefone", "login", "senha")
                .containsExactly(
                        usuarioEsperado.getNome(),
                        usuarioEsperado.getEmail(),
                        usuarioEsperado.getCpf(),
                        usuarioEsperado.getCnpj(),
                        usuarioEsperado.getTelefone(),
                        usuarioEsperado.getLogin(),
                        usuarioEsperado.getSenha()
                );
    }

    @Test
    void deveConverterDominioParaDto() {
        Usuario dominio = UsuarioTestFactory.criarUsuarioDomain();
        UsuarioResponseDTO dtoEsperado = UsuarioTestFactory.criarUsuarioResponseDTO();

        UsuarioResponseDTO resultado = UsuarioMapper.toResponseDTO(dominio);

        assertThat(resultado)
                .isNotNull()
                .extracting( "nome", "email")
                .containsExactly(
                        dtoEsperado.getNome(),
                        dtoEsperado.getEmail()
                );
    }

    @Test
    void deveConverterDominioParaEntidade() {
        Usuario usuario = UsuarioTestFactory.criarUsuarioDomainComId();
        UsuarioEntity entityEsperada = UsuarioTestFactory.criarUsuarioEntity();

        UsuarioEntity resultado = UsuarioMapper.toEntity(usuario);

        assertThat(resultado)
                .isNotNull()
                .extracting("nome", "email", "cpf", "cnpj", "telefone", "login", "senha", "ativo")
                .containsExactly(
                        entityEsperada.getNome(),
                        entityEsperada.getEmail(),
                        entityEsperada.getCpf(),
                        entityEsperada.getCnpj(),
                        entityEsperada.getTelefone(),
                        entityEsperada.getLogin(),
                        entityEsperada.getSenha(),
                        entityEsperada.isAtivo()
                );
    }

    @Test
    void deveConverterEntidadeParaDominio() {
        UsuarioEntity entity = UsuarioTestFactory.criarUsuarioEntity();
        Usuario usuarioEsperado = UsuarioTestFactory.criarUsuarioDomainComId();

        Usuario resultado = UsuarioMapper.toDomainDto(entity);

        assertThat(resultado)
                .isNotNull()
                .extracting("id", "nome", "email", "cpf", "cnpj", "telefone", "login", "senha", "ativo")
                .containsExactly(
                        usuarioEsperado.getId(),
                        usuarioEsperado.getNome(),
                        usuarioEsperado.getEmail(),
                        usuarioEsperado.getCpf(),
                        usuarioEsperado.getCnpj(),
                        usuarioEsperado.getTelefone(),
                        usuarioEsperado.getLogin(),
                        usuarioEsperado.getSenha(),
                        usuarioEsperado.isAtivo()
                );
    }
}