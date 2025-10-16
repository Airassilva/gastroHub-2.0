package pos.tech.cleanarchandjpa.infra.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "cardapio")
@Getter
@Setter
@NoArgsConstructor
public class CardapioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @NotBlank(message = "O campo nome é obrigatório.")
    private String nome;

    private String descricao;

    private double preco;

    private DisponibilidadeConsumo disponibilidade;

    private String caminhoImagem;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private RestauranteEntity restaurante;

    public CardapioEntity(UUID id, String nome, String descricao, double preco, String caminhoImagem, RestauranteEntity restaurante) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.caminhoImagem = caminhoImagem;
        this.restaurante = restaurante;
        this.dataAtualizacao = new Date();
        this.disponibilidade = DisponibilidadeConsumo.APENAS_NO_RESTAURANTE;
    }
}
