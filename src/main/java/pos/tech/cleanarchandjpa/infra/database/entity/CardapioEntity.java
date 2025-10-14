package pos.tech.cleanarchandjpa.infra.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "cardapio")
@Getter
@Setter
public class CardapioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório.")
    private String nome;

    private String descricao;

    @NotBlank(message = "O campo preço é obrigatório.")
    private double preco;

    private DisponibilidadeConsumo disponibilidade;

    public CardapioEntity(UUID id, String nome, double preco, String descricao, DisponibilidadeConsumo disponibilidade, String caminhoImagem, RestauranteEntity restaurante) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.disponibilidade = disponibilidade;
        this.caminhoImagem = caminhoImagem;
        this.dataAtualizacao = new Date();
        this.restaurante = restaurante;
    }

    private String caminhoImagem;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private RestauranteEntity restaurante;
}
