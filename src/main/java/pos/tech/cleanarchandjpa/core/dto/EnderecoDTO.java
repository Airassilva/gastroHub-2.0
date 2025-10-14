package pos.tech.cleanarchandjpa.core.dto;

public record EnderecoDTO(
        String bairro,

        String cep,

        String cidade,

        String estado,

        String rua,

        String complemento,

        String numero) {
}
