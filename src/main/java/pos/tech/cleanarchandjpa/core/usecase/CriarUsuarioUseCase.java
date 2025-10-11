package pos.tech.cleanarchandjpa.core.usecase;

import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.UsuarioOutput;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioJaExisteException;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;


public class CriarUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    public CriarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public UsuarioOutput cadastrarUsuario(Usuario usuario) throws DadosInvalidosException, UsuarioJaExisteException {
        validarDocumentoObrigatorio(usuario);
        verificarDuplicidade(usuario);

        usuarioGateway.criarUsuario(usuario);

        return new UsuarioOutput(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    private void validarDocumentoObrigatorio(Usuario usuario) throws DadosInvalidosException {
        boolean cpfValido = usuario.getCpf() != null && !usuario.getCpf().isBlank();
        boolean cnpjValido = usuario.getCnpj() != null && !usuario.getCnpj().isBlank();

        if (!cpfValido && !cnpjValido) {
            throw new DadosInvalidosException("CPF ou CNPJ devem ser informados.");
        }
    }

    private void verificarDuplicidade(Usuario usuario) throws UsuarioJaExisteException {
        if (usuario.getCpf() != null && !usuario.getCpf().isBlank()
                && usuarioGateway.buscarUsuarioPorCpf(usuario).isPresent()) {
                throw new UsuarioJaExisteException("Usuário com este CPF já cadastrado.");
        }

        if (usuario.getCnpj() != null && !usuario.getCnpj().isBlank()
                && usuarioGateway.buscarUsuarioPorCnpj(usuario).isPresent()) {
                throw new UsuarioJaExisteException("Usuário com este CNPJ já cadastrado.");
        }
    }
}
