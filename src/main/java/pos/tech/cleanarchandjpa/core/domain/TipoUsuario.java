package pos.tech.cleanarchandjpa.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class TipoUsuario {
   private UUID id;
   private String nomeTipoUsuario;
   private List<Usuario> usuarios = new ArrayList<>();
   private boolean ativo;

    public TipoUsuario(UUID id, String nomeTipoUsuario, List<Usuario> usuarios) {
        this.id = id;
        this.nomeTipoUsuario = nomeTipoUsuario;
        this.usuarios = usuarios;
        ativo = true;
    }

    public TipoUsuario(UUID id, String tipoUsuario) {
        this.id = id;
        this.nomeTipoUsuario = tipoUsuario;
    }

    public TipoUsuario adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
        usuario.atribuirTipoUsuario(this);
        return this;
    }

    public TipoUsuario comNovosDados(TipoUsuario novo) {
        validarTipoUsuario(novo);

        return new TipoUsuario(
                this.id,
                atualizarCampo(novo.nomeTipoUsuario, this.nomeTipoUsuario),
                novo.usuarios != null ? novo.usuarios : this.usuarios
        );
    }

    private String atualizarCampo(String novoValor, String valorAtual) {
        return (novoValor != null && !novoValor.isBlank()) ? novoValor : valorAtual;
    }

    private void validarTipoUsuario(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            throw new IllegalArgumentException("Tipo de usuário não pode ser nulo");
        }

        validarCampoNaoVazio(tipoUsuario.nomeTipoUsuario, "Nome do tipo de usuário");
    }

    private void validarCampoNaoVazio(String campo, String nomeCampo) {
        if (campo != null && campo.isBlank()) {
            throw new IllegalArgumentException(nomeCampo + " não pode estar vazio");
        }
    }
}
