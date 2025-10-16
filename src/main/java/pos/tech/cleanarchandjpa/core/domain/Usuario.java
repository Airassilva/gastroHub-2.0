package pos.tech.cleanarchandjpa.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    public static final String CAMPO_SENHA = "senha";
    public static final String CAMPO_ENDERECO = "endereço";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_LOGIN = "login";


    public Usuario(UUID id, String email, Endereco endereco, String login, String senha, TipoUsuario tipoUsuario, String cpf, String cnpj) {
        this.id = id;
        this.email = validateNotBlank(email, CAMPO_EMAIL);
        this.endereco = validateNotNull(endereco, CAMPO_ENDERECO);
        this.login = validateNotBlank(login, CAMPO_LOGIN);
        this.senha = validateNotBlank(senha, CAMPO_SENHA);
        this.tipoUsuario = tipoUsuario;
        if (isBlankBoth(cpf, cnpj)) {
            throw new BadRequestException("Precisa ser informado um CPF ou CNPJ.");
        }else {
            this.cpf = cpf;
            this.cnpj = cnpj;
        }
    }

    public Usuario(UUID id, String email, Endereco endereco, String login, String senha) {
        this.id = id;
        this.email = validateNotBlank(email, CAMPO_EMAIL);
        this.endereco = validateNotNull(endereco, CAMPO_ENDERECO);
        this.login = validateNotBlank(login, CAMPO_LOGIN);
        this.senha = validateNotBlank(senha, CAMPO_SENHA);
    }

    public Usuario(UUID id, String email, String login, String senha, String cpf, String cnpj) {
        this.id = id;
        this.email = validateNotBlank(email, CAMPO_EMAIL);
        this.login = validateNotBlank(login, CAMPO_LOGIN);
        this.senha = validateNotBlank(senha, CAMPO_SENHA);
        if (isBlankBoth(cpf, cnpj)) {
            throw new BadRequestException("Precisa ser informado um CPF ou CNPJ.");
        }else {
            this.cpf = cpf;
            this.cnpj = cnpj;
        }
    }

    public Usuario(UUID id, String email, Endereco endereco,String login,String senha, TipoUsuario tipoUsuario, String cpf, String cnpj, String telefone) {
        this.id = id;
        this.email = validateNotBlank(email, CAMPO_EMAIL);
        this.endereco = validateNotNull(endereco, CAMPO_ENDERECO);
        this.login = validateNotBlank(login, CAMPO_LOGIN);
        this.senha = validateNotBlank(senha, CAMPO_SENHA);
        this.tipoUsuario = tipoUsuario;
        if (isBlankBoth(cpf, cnpj)) {
            throw new BadRequestException("Precisa ser informado um CPF ou CNPJ.");
        }else {
            this.cpf = cpf;
            this.cnpj = cnpj;
        }
        this.telefone = validateNotBlank(telefone, "telefone");
    }

    public void atualizarDadosBasicos(Usuario usuario) {
        atualizeSePresente(usuario.getEmail(), emailP -> this.email = emailP);
        atualizeSePresente(usuario.getTelefone(), telefoneP -> this.telefone = telefoneP);
        atualizeSePresente(usuario.getLogin(), loginP -> this.login = loginP);
        atualizeSePresente(usuario.getSenha(), senhaP -> this.senha = senhaP);
        this.dataUltimaAlteracao = new Date();
    }
    public Usuario(UUID id, String nome, String cpf, String cnpj, String email, String telefone, String login, String senha, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        if (isBlankBoth(cpf, cnpj)) {
            throw new BadRequestException("Precisa ser informado um CPF ou CNPJ.");
        }else {
            this.cpf = cpf;
            this.cnpj = cnpj;
        }
        atualizeSePresente(email, emailP -> this.email = emailP);
        atualizeSePresente(telefone, telefoneP -> this.telefone = telefoneP);
        atualizeSePresente(login, loginP -> this.login = loginP);
        atualizeSePresente(senha, senhaP -> this.senha = senhaP);
        this.dataUltimaAlteracao = new Date();
        this.ativo = true;
        this.endereco = endereco;
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

    public void atribuirTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}