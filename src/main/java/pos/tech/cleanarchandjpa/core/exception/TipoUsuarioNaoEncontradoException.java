package pos.tech.cleanarchandjpa.core.exception;

public class TipoUsuarioNaoEncontradoException extends DomainException {
    public TipoUsuarioNaoEncontradoException() {
        super("Tipo de usuário não encontrado");
    }
}
