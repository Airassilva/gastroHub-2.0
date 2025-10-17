package pos.tech.cleanarchandjpa.core.exception;

public class CardapioNaoEncontradoException extends DomainException {
    public CardapioNaoEncontradoException() {
        super("Restaurante não encontrado");
    }
}
