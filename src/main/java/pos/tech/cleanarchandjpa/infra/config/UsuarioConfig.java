package pos.tech.cleanarchandjpa.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.core.usecase.AtualizarEnderecoUseCase;
import pos.tech.cleanarchandjpa.core.usecase.usuario.*;
import pos.tech.cleanarchandjpa.infra.database.repository.UsuarioRepository;
import pos.tech.cleanarchandjpa.infra.gateway.UsuarioRepositoryGateway;


@Configuration
public class UsuarioConfig {

    @Bean
    public UsuarioGateway usuarioGateway(UsuarioRepository jpaRepository) {
        return new UsuarioRepositoryGateway(jpaRepository);
    }

    @Bean
    public ListaDeUsuariosUseCase listaDeUsuariosUseCase(UsuarioGateway usuarioGateway) {
        return new ListaDeUsuariosUseCase(usuarioGateway);
    }

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        return new CriarUsuarioUseCase(usuarioGateway);
    }


    @Bean
    public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        return new DeletarUsuarioUseCase(usuarioGateway);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        return new AtualizarUsuarioUseCase(usuarioGateway);
    }

    @Bean
    public AtualizarUsuarioComEnderecoUseCase atualizarUsuarioComEnderecoUseCase(
            AtualizarUsuarioUseCase atualizarUsuarioUseCase,
            AtualizarEnderecoUseCase atualizarEnderecoUseCase) {
        return new AtualizarUsuarioComEnderecoUseCaseImpl(
                atualizarUsuarioUseCase,
                atualizarEnderecoUseCase
        );
    }
}
