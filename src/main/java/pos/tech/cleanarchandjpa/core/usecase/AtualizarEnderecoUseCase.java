package pos.tech.cleanarchandjpa.core.usecase;

import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.PossuiEndereco;
import pos.tech.cleanarchandjpa.core.gateway.EnderecoGateway;

public class AtualizarEnderecoUseCase {

    private final EnderecoGateway enderecoGateway;

    public AtualizarEnderecoUseCase(EnderecoGateway enderecoGateway) {
        this.enderecoGateway = enderecoGateway;
    }

    public  <T extends PossuiEndereco> void atualizarEnderecoSeNecessario(T entidade, T entidadeAtualizada) {
        if (entidadeAtualizada.getEndereco() != null) {
            Endereco enderecoExistente = entidade.getEndereco();

            if (enderecoExistente != null) {
                enderecoExistente.atualizarEndereco(entidadeAtualizada.getEndereco());
                enderecoGateway.salvar(enderecoExistente);
            } else {
                Endereco novoEndereco = entidadeAtualizada.getEndereco();
                Endereco enderecoCriado = enderecoGateway.salvar(novoEndereco);
                entidade.atribuirEndereco(enderecoCriado);
            }
        }
    }
}
