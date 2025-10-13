package pos.tech.cleanarchandjpa.core.usecase;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidarDocumentosUseCase {

    public static void validarDocumentoObrigatorio(Usuario usuario) throws DadosInvalidosException {
        boolean cpfValido = usuario.getCpf() != null && !usuario.getCpf().isBlank();
        boolean cnpjValido = usuario.getCnpj() != null && !usuario.getCnpj().isBlank();

        if (!cpfValido && !cnpjValido) {
            throw new DadosInvalidosException("CPF ou CNPJ devem ser informados.");
        }
    }
}
