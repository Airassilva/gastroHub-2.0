package pos.tech.cleanarchandjpa.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pos.tech.cleanarchandjpa.core.gateway.TipoUsuarioGateway;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.AtualizarTiposDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.CriarTipoDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.DeletarTipoDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.core.usecase.tipousuario.ListarTiposDeUsuarioUseCase;
import pos.tech.cleanarchandjpa.infra.database.repository.TipoUsuarioRepository;
import pos.tech.cleanarchandjpa.infra.gateway.TipoUsuarioRepositoryGateway;

@Configuration
public class TipoUsuarioConfig {
    @Bean
    public TipoUsuarioGateway tipoUsuarioGateway(TipoUsuarioRepository tipoUsuarioRepository) {
        return new TipoUsuarioRepositoryGateway(tipoUsuarioRepository);
    }

    @Bean
    public CriarTipoDeUsuarioUseCase criarTipoDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway, UsuarioGateway usuarioGateway) {
        return new CriarTipoDeUsuarioUseCase(tipoUsuarioGateway, usuarioGateway);
    }

    @Bean
    public AtualizarTiposDeUsuarioUseCase atualizarTiposDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        return new AtualizarTiposDeUsuarioUseCase(tipoUsuarioGateway);
    }

    @Bean
    public ListarTiposDeUsuarioUseCase listarTiposDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        return new ListarTiposDeUsuarioUseCase(tipoUsuarioGateway);
    }

    @Bean
    public DeletarTipoDeUsuarioUseCase deletarTipoDeUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        return new DeletarTipoDeUsuarioUseCase(tipoUsuarioGateway);
    }
}
