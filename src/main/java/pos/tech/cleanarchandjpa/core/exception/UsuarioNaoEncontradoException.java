package pos.tech.cleanarchandjpa.core.exception;

public class UsuarioNaoEncontradoException extends DomainException {
    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado");
    }
}
