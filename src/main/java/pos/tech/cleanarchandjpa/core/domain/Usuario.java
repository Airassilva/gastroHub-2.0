package pos.tech.cleanarchandjpa.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.exception.BadRequestException;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Usuario {
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

    public Usuario(String cpf, String cnpj) {
        if ((cpf == null || cpf.isBlank()) && (cnpj == null || cnpj.isBlank())) {
            throw new BadRequestException("Precisa ser informado um CPF ou CNPJ.");
        }
        this.cpf = cpf;
        this.cnpj = cnpj;
    }

    public Usuario(String nome, String email, String cpf, String cnpj, String telefone, String login, String senha, TipoUsuario tipoUsuario, Endereco endereco) {
        if(email == null || email.isBlank()) {
            throw new BadRequestException("O campo email é obrigatório.");
        }
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.endereco = endereco;
    }

    public Usuario(UUID id, String email, Endereco endereco) {
        this.id = id;
        this.email = email;
        this.endereco = endereco;
    }
}
