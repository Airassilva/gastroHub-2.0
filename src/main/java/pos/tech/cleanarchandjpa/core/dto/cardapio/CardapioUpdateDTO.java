package pos.tech.cleanarchandjpa.core.dto.cardapio;

public record CardapioUpdateDTO (
        String nome,
        double preco,
        String descricao,
        String caminhoFoto ){
}
