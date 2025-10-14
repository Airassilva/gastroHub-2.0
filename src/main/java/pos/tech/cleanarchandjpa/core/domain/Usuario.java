package pos.tech.cleanarchandjpa.core.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import pos.tech.cleanarchandjpa.core.exception.BadRequestException;

import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Usuario implements PossuiEndereco{
    private UUID id;
    private String nome;
    private String email;
    private String cpf;
    private String cnpj;
    private String telefone;
    private boolean ativo;
    private Date dataUltimaAlteracao;
    private Date dataCriacao;
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario;
    private Endereco endereco;

    public Usuario(UUID id, String email, Endereco endereco, String login, String senha) {
        this.id = id;
        this.email = validateNotBlank(email, "email");
        this.endereco = validateNotNull(endereco, "endereço");
        this.login = validateNotBlank(login, "login");
        this.senha = validateNotBlank(senha, "senha");
    }

    public void atualizarDadosBasicos(Usuario usuario) {
        atualizeSePresente(usuario.getEmail(), emailP -> this.email = emailP);
        atualizeSePresente(usuario.getTelefone(), telefoneP -> this.telefone = telefoneP);
        atualizeSePresente(usuario.getLogin(), loginP -> this.login = loginP);
        atualizeSePresente(usuario.getSenha(), senhaP -> this.senha = senhaP);
        this.dataUltimaAlteracao = new Date();
    }

    public Usuario(UUID id, String nome, String email, String cpf, String cnpj, String telefone, String login, String senha, Endereco endereco) {
        this.id = id;
        this.nome = validateNotBlank(nome, "nome");
        this.email = validateNotBlank(email, "email");
        if (isBlankBoth(cpf, cnpj)) {
            throw new BadRequestException("Precisa ser informado um CPF ou CNPJ.");
        }
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.telefone = validateNotBlank(telefone, "telefone");
        this.login = validateNotBlank(login, "login");
        this.senha = validateNotBlank(senha, "senha");
        this.endereco = validateNotNull(endereco, "endereco");
        dataUltimaAlteracao = new Date();
    }

    private static boolean isBlankBoth(String cpf, String cnpj) {
        return (cpf == null || cpf.isBlank()) && (cnpj == null || cnpj.isBlank());
    }

    private static String validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("Precisa ser informado um " + fieldName + ".");
        }
        return value;
    }

    private static <T> T validateNotNull(T value, String fieldName) {
        if (value == null) {
            throw new BadRequestException("Precisa ser informado um " + fieldName + ".");
        }
        return value;
    }

    private static void atualizeSePresente(String value, Consumer<String> updater) {
        if (value != null && !value.isBlank()) {
            updater.accept(value);
        }
    }

    @Override
    public Endereco getEndereco() {
        return endereco;
    }

    @Override
    public void atribuirEndereco(Endereco novoEndereco) {
        this.endereco = novoEndereco;
    }
}