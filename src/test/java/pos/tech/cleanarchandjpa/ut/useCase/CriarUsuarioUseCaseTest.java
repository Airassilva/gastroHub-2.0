package pos.tech.cleanarchandjpa.ut.useCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.UsuarioOutput;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioJaExisteException;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.core.usecase.usuario.CriarUsuarioUseCase;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private CriarUsuarioUseCase criarUsuarioUseCase;

    TipoUsuario tipo = new TipoUsuario(new UUID(0L, 0L), "dono");
    Endereco endereco = new Endereco(new UUID(1L,1L),"Rua das Flores", "123", "Recife", "PE", "50000-000");

    Usuario usuario1 = new Usuario(
            "Aira Soares",
            "aira@email.com",
            "12345678900",
            null,
            "(81) 99999-9999",
            "aira_login",
            "senhaSegura123",
            tipo,
            endereco
    );

    Usuario usuario2 = new Usuario(
            "Aira Soares",
            "aira@email.com",
            null,
            null,
            "(81) 99999-9999",
            "aira_login",
            "senhaSegura123",
            tipo,
            endereco
    );

    Usuario usuario3 = new Usuario(
            "Aira Soares",
            "aira@email.com",
            null,
            "1234567890",
            "(81) 99999-9999",
            "aira_login",
            "senhaSegura123",
            tipo,
            endereco
    );

    @Test
    void deveCadastrarUsuarioComCpfValido() throws UsuarioJaExisteException, DadosInvalidosException {
        Mockito.when(usuarioGateway.buscarUsuarioPorCpf(usuario1))
                .thenReturn(Optional.empty());

        UsuarioOutput output = criarUsuarioUseCase.cadastrarUsuario(usuario1);

        assertEquals("Aira Soares", output.getNome());
        assertEquals("aira@email.com", output.getEmail());
        Mockito.verify(usuarioGateway).criarUsuario(usuario1);
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        Mockito.when(usuarioGateway.buscarUsuarioPorCpf(usuario1))
                .thenReturn(Optional.of(usuario1));

        assertThrows(UsuarioJaExisteException.class, () -> {
            criarUsuarioUseCase.cadastrarUsuario(usuario1);
        });

        Mockito.verify(usuarioGateway, Mockito.never()).criarUsuario(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoCnpjJaExiste() {
        Mockito.when(usuarioGateway.buscarUsuarioPorCnpj(usuario3))
                .thenReturn(Optional.of(usuario3));

        assertThrows(UsuarioJaExisteException.class, () -> {
            criarUsuarioUseCase.cadastrarUsuario(usuario3);
        });

        Mockito.verify(usuarioGateway, Mockito.never()).criarUsuario(Mockito.any());
    }

    @Test
    void deveLancarExcecaoQuandoCpfECnpjNaoInformados() {
        assertThrows(DadosInvalidosException.class, () -> {
            criarUsuarioUseCase.cadastrarUsuario(usuario2);
        });
        Mockito.verify(usuarioGateway, Mockito.never()).criarUsuario(Mockito.any());
    }
}