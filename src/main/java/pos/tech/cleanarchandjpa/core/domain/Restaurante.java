package pos.tech.cleanarchandjpa.core.domain;

import lombok.Getter;
import pos.tech.cleanarchandjpa.core.exception.PermissaoNegadaException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public Restaurante(UUID uuid, String nome, String tipoCozinha, Endereco endereco, List<HorarioFuncionamento> horarios, Usuario usuario, Date dataCadastro, Date dataAtualizacao) {
        this.id = uuid;
        this.nome = nome;
        this.tipoCozinha = tipoCozinha;
        this.endereco = endereco;
        this.horariosFunc = horarios;
        this.usuario = usuario;
        this.dataCadastro = dataCadastro;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Restaurante(String tipoCozinha, Endereco endereco, List<HorarioFuncionamento> horariosFunc, Date dataAtualizacao) {
        this.tipoCozinha = tipoCozinha;
        this.endereco = endereco;
        this.horariosFunc = horariosFunc;
        this.dataAtualizacao = dataAtualizacao;
    }

    public boolean estaAberto() {
        LocalDateTime agora = LocalDateTime.now();
        DayOfWeek diaAtual = agora.getDayOfWeek();
        LocalTime horaAtual = agora.toLocalTime();

        return horariosFunc.stream()
                .filter(h -> h.getDiaSemana() == diaAtual)
                .anyMatch(h -> h.estaAberto(horaAtual));
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

    public Restaurante comDono(Usuario dono) {
        validarDono(dono);

        return new Restaurante(
                this.id,
                this.nome,
                this.tipoCozinha,
                this.endereco,
                this.horariosFunc,
                dono,
                this.dataAtualizacao,
                this.dataCadastro
        );
    }

    private void validarDono(Usuario dono) {
        if (dono == null) {
            throw new IllegalArgumentException("Dono não pode ser nulo");
        }

        var ehDono = usuario.getTipoUsuario().getUsuario().stream()
                .anyMatch(tipo -> tipo.getTipoUsuario().getNomeTipoUsuario().equals("DONO"));

        if (!ehDono) {
            throw new PermissaoNegadaException("Apenas usuários do tipo DONO podem criar restaurantes");
        }
    }
}
