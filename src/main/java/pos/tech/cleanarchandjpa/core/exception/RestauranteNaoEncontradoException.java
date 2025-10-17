package pos.tech.cleanarchandjpa.core.exception;

public class RestauranteNaoEncontradoException extends DomainException {
    public RestauranteNaoEncontradoException() {
        super("Restaurante não encontrado");
    }
}
