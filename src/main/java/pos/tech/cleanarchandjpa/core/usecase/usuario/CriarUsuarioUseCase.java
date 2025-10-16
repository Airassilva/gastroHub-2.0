package pos.tech.cleanarchandjpa.core.usecase.usuario;

import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.exception.DadosInvalidosException;
import pos.tech.cleanarchandjpa.core.exception.UsuarioJaExisteException;
import pos.tech.cleanarchandjpa.core.gateway.UsuarioGateway;
import pos.tech.cleanarchandjpa.core.usecase.ValidarDocumentosUseCase;

public class CriarUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    public CriarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario cadastrarUsuario(Usuario usuario) throws DadosInvalidosException, UsuarioJaExisteException {
        ValidarDocumentosUseCase.validarDocumentoObrigatorio(usuario);
        verificarDuplicidade(usuario);
        return usuarioGateway.criarUsuario(usuario);
    }

    private void verificarDuplicidade(Usuario usuario) throws UsuarioJaExisteException {
        if (usuario.getCpf() != null && !usuario.getCpf().isBlank()
                && usuarioGateway.buscarUsuarioPorCpf(usuario) != null) {
                throw new UsuarioJaExisteException("Usuário com este CPF já cadastrado.");
        }

        if (usuario.getCnpj() != null && !usuario.getCnpj().isBlank()
                && usuarioGateway.buscarUsuarioPorCnpj(usuario) != null) {
                throw new UsuarioJaExisteException("Usuário com este CNPJ já cadastrado.");
        }
    }
}
