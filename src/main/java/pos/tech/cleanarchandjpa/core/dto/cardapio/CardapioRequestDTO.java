package pos.tech.cleanarchandjpa.core.dto.cardapio;

public class CardapioRequestDTO {
    String nome;
    String descricao;
    double preco;
    String caminhoFoto;

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco() {
        return preco;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }
}
