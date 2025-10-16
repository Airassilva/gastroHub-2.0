package pos.tech.cleanarchandjpa.core.domain;

import lombok.Getter;
import pos.tech.cleanarchandjpa.core.exception.PermissaoNegadaException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class Restaurante implements PossuiEndereco{
    private UUID id;
    private String nome;
    private String tipoCozinha;
    private Endereco endereco;
    private List<HorarioFuncionamento> horariosFunc;
    private List<Cardapio> cardapios;
    private Usuario usuario;
    private Date dataCadastro;
    private Date dataAtualizacao;

    public Restaurante(UUID id, String nome, String tipoCozinha, Endereco endereco, List<HorarioFuncionamento> horarios, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.tipoCozinha = tipoCozinha;
        this.endereco = endereco;
        this.horariosFunc = horarios;
        this.usuario = usuario;
        this.dataCadastro = new Date();
        this.dataAtualizacao = new Date();
    }

    public Restaurante(UUID id, String nome, String tipoCozinha, Endereco endereco, List<HorarioFuncionamento> horariosFunc, List<Cardapio> cardapios, Usuario usuario) {
        this.id = id;
        this.nome = nome;
        this.tipoCozinha = tipoCozinha;
        this.endereco = endereco;
        this.horariosFunc = horariosFunc;
        this.cardapios = cardapios;
        this.usuario = usuario;
        this.dataAtualizacao = new Date();
    }

    public Restaurante(String tipoCozinha, Endereco endereco, List<HorarioFuncionamento> horarios) {
        this.tipoCozinha = tipoCozinha;
        this.endereco = endereco;
        this.horariosFunc = horarios;
        this.dataAtualizacao = new Date();
    }

    public void atualizarDados(Restaurante restaurante) {
        atualizeSePresente(restaurante.tipoCozinha, tipoCozinhaP -> this.tipoCozinha = tipoCozinhaP);
        atualizeSePresenteLista(restaurante.horariosFunc, horariosFuncP -> this.horariosFunc = horariosFuncP);
        this.dataAtualizacao = new Date();
    }

    private static void atualizeSePresente(String value, Consumer<String> updater) {
        if (value != null && !value.isBlank()) {
            updater.accept(value);
        }
    }

    private static <T> void atualizeSePresenteLista(List<T> value, Consumer<List<T>> updater) {
        if (value != null && !value.isEmpty()) {
            updater.accept(value);
        }
    }

    @Override
    public void atribuirEndereco(Endereco enderecoCriado) {
        this.endereco = enderecoCriado;
    }

    @Override
    public Endereco getEndereco() {
        return endereco;
    }

    public Restaurante comDono(TipoUsuario tipoUsuario, Usuario dono) {
        validarDono(tipoUsuario);

        return new Restaurante(
                this.id,
                this.nome,
                this.tipoCozinha,
                this.endereco,
                this.horariosFunc,
                dono
        );
    }

    public void adicionarCardapio(Cardapio novoCardapio) {
        if (this.cardapios == null) {
            this.cardapios = new ArrayList<>();
        }
        this.cardapios.add(novoCardapio);
        this.dataAtualizacao = new Date();
    }


    private void validarDono(TipoUsuario dono) {
        if (dono == null) {
            throw new IllegalArgumentException("Dono não pode ser nulo");
        }
        if (!dono.getNomeTipoUsuario().equalsIgnoreCase("dono")) {
            throw new PermissaoNegadaException("Apenas usuários do tipo DONO podem criar restaurantes");
        }
    }
}
