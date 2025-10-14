package pos.tech.cleanarchandjpa.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Endereco {
    private UUID id;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String complemento;
    private Date dataUltimaAlteracao;

    public Endereco(UUID id, String rua, String numero, String cidade, String estado,String cep) {
        this.id = id;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    public Endereco( String cep, String rua, String numero, String cidade, String estado) {
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
    }

    public void atualizarEndereco(Endereco endereco) {
        validarEndereco(endereco);

        this.rua = endereco.getRua();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.cep = endereco.getCep();
        this.bairro = atualizarCampoOpcional(endereco.getBairro(), this.bairro);
        this.numero = atualizarCampoOpcional(endereco.getNumero(), this.numero);
        this.complemento = atualizarCampoOpcional(endereco.getComplemento(), this.complemento);
        this.dataUltimaAlteracao = new Date();
    }

    private void validarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }

        validarCampoObrigatorio(endereco.getRua(), "Rua");
        validarCampoObrigatorio(endereco.getCidade(), "Cidade");
        validarCampoObrigatorio(endereco.getEstado(), "Estado");
        validarCampoObrigatorio(endereco.getCep(), "CEP");
        validarFormatoCep(endereco.getCep());
    }

    private void validarCampoObrigatorio(String campo, String nomeCampo) {
        if (campo == null || campo.isBlank()) {
            throw new IllegalArgumentException(nomeCampo + " é obrigatório");
        }
    }

    private void validarFormatoCep(String cep) {
        if (!cep.matches("\\d{5}-\\d{3}")) {
            throw new IllegalArgumentException("CEP deve estar no formato XXXXX-XXX");
        }
    }

    private String atualizarCampoOpcional(String novoValor, String valorAtual) {
        return (novoValor != null && !novoValor.isBlank()) ? novoValor : valorAtual;
    }
}
