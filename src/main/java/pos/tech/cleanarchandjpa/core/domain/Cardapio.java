package pos.tech.cleanarchandjpa.core.domain;

import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class Cardapio {
    private UUID id;
    private String nome;
    private String descricao;
    private double preco;
    private DisponibilidadeConsumo disponibilidade;
    private String caminhoImagem;
    private Date dataAtualizacao;
    private Restaurante restaurante;

    public Cardapio(UUID id, String nome, String descricao, double preco, String caminhoImagem, Restaurante novoRestaurante) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.caminhoImagem = caminhoImagem;
        this.restaurante = novoRestaurante;
        this.dataAtualizacao = new Date();
        this.disponibilidade = DisponibilidadeConsumo.APENAS_NO_RESTAURANTE;
    }

    public Cardapio(String nome, String descricao, double preco, String caminhoFoto) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.caminhoImagem = caminhoFoto;
        this.dataAtualizacao = new Date();
        this.disponibilidade = DisponibilidadeConsumo.APENAS_NO_RESTAURANTE;
    }

    public Cardapio comRestaurante(Restaurante novoRestaurante) {
        return new Cardapio(
                this.id,
                this.nome,
                this.descricao,
                this.preco,
                this.caminhoImagem,
                novoRestaurante);
    }

    public Cardapio comNovosDados(Cardapio novo) {
        validarCardapio(novo);

        return new Cardapio(
                this.id,
                atualizaSePresente(novo.nome, this.nome),
                atualizaSePresente(novo.descricao, this.descricao),
                novo.preco > 0 ? novo.preco : this.preco,
                atualizaSePresente(novo.caminhoImagem, this.caminhoImagem),
                this.restaurante
        );
    }

    private String atualizaSePresente(String novoValor, String valorAtual) {
        return (novoValor != null && !novoValor.isBlank()) ? novoValor : valorAtual;
    }

    private void validarCardapio(Cardapio cardapio) {
        if (cardapio == null) {
            throw new IllegalArgumentException("Cardápio não pode ser nulo");
        }

        if (cardapio.preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        validarCampoNaoVazio(cardapio.nome, "Nome");
        validarCampoNaoVazio(cardapio.descricao, "Descrição");
        validarCampoNaoVazio(cardapio.caminhoImagem, "Caminho da imagem");
    }

    private void validarCampoNaoVazio(String campo, String nomeCampo) {
        if (campo != null && campo.isBlank()) {
            throw new IllegalArgumentException(nomeCampo + " não pode estar vazio");
        }
    }
}
