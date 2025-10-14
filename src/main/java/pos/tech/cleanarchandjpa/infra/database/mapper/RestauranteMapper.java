package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pos.tech.cleanarchandjpa.core.domain.Endereco;
import pos.tech.cleanarchandjpa.core.domain.HorarioFuncionamento;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.restaurante.HorarioFuncionamentoDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteRequestDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteUpdateDTO;
import pos.tech.cleanarchandjpa.core.exception.BadRequestException;
import pos.tech.cleanarchandjpa.infra.database.entity.EnderecoEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.HorarioFuncionamentoEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.RestauranteEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestauranteMapper {


    public static Restaurante paraDominioDeRequest(RestauranteRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        List<HorarioFuncionamento> horarios = requestDTO.horarios().stream()
                .map(h -> new HorarioFuncionamento(
                        h.diaSemana(),
                        LocalTime.parse(h.abertura()),
                        LocalTime.parse(h.fechamento())
                ))
                .toList();
        var endereco =  EnderecoMapper.deDtoParaDominio(requestDTO.endereco());

        return new Restaurante(
                requestDTO.tipoDeCozinha(),
                endereco,
                horarios,
                new Date()
        );
    }

    public static List<HorarioFuncionamento> toHorarioFuncionamentoList(List<HorarioFuncionamentoDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(h -> {
                    try {
                        LocalTime abertura = LocalTime.parse(h.abertura());
                        LocalTime fechamento = LocalTime.parse(h.fechamento());

                        if (!abertura.isBefore(fechamento)) {
                            throw new BadRequestException("Hora de abertura deve ser antes do fechamento");
                        }

                        return new HorarioFuncionamento(
                                h.diaSemana(),
                                abertura,
                                fechamento
                        );
                    } catch (DateTimeParseException _) {
                        throw new BadRequestException("Formato de hora inválido. Use HH:mm");
                    }
                })
                .toList();
    }

    public static Restaurante paraDominioDeDTO(RestauranteUpdateDTO updateDTO) {
        var endereco = EnderecoMapper.deDtoParaDominio(updateDTO.enderecoDTO());
        var horarios = RestauranteMapper.toHorarioFuncionamentoList(updateDTO.horarioFuncionamentoDTO());

        return new Restaurante(
                updateDTO.tipoDeCozinha(),
                endereco,
                horarios,
                new Date()
        );
    }

    public static RestauranteResponseDTO paraResponseDTO(Restaurante restaurante) {
        if (restaurante == null) return null;

        UUID idUsuarioDono = restaurante.getUsuario() != null
                ? restaurante.getUsuario().getId()
                : null;

        return new RestauranteResponseDTO(
                restaurante.getId(),
                restaurante.getNome(),
                idUsuarioDono
        );
    }

    public static Restaurante paraDominioOptional(Optional<RestauranteEntity> entity) {
        Endereco enderecoDomain = null;
        if (entity.get().getEndereco() != null) {
            enderecoDomain = new Endereco(
                    entity.get().getEndereco().getId(),
                    entity.get().getEndereco().getRua(),
                    entity.get().getEndereco().getNumero(),
                    entity.get().getEndereco().getCidade(),
                    entity.get().getEndereco().getEstado(),
                    entity.get().getEndereco().getCep()
            );
        }

        Usuario usuarioDomain = null;
        if (entity.get().getDono() != null) {
            usuarioDomain = new Usuario(
                    entity.get().getDono().getId(),
                    entity.get().getDono().getNome(),
                    entity.get().getDono().getEmail(),
                    entity.get().getDono().getCpf(),
                    entity.get().getDono().getCnpj(),
                    entity.get().getDono().getTelefone(),
                    entity.get().getDono().isAtivo(),
                    entity.get().getDono().getDataCriacao(),
                    entity.get().getDono().getDataUltimaAlteracao(),
                    entity.get().getDono().getLogin(),
                    entity.get().getDono().getSenha(),
                    null,
                    null
            );
        }

        List<HorarioFuncionamento> horarios = entity.get().getHorarios().stream()
                .map(h -> new HorarioFuncionamento(
                        h.getDiaSemana(),
                        h.getAbertura(),
                        h.getFechamento()
                ))
                .toList();

        return new Restaurante(
                entity.get().getId(),
                entity.get().getNome(),
                entity.get().getTipoDeCozinha(),
                enderecoDomain,
                horarios,
                usuarioDomain,
                entity.get().getDataCriacao(),
                entity.get().getDataAtualizacao()
        );
    }

    public static Restaurante paraDominio(RestauranteEntity entity) {
        var enderecoDomain = entity.getEndereco() != null
                ? EnderecoMapper.paraDominio(Optional.of(entity.getEndereco()))
                : null;

        var usuarioDomain = entity.getDono() != null
                ? UsuarioMapper.paraDominioDeOptional(Optional.of(entity.getDono()))
                : null;

        var horarios = entity.getHorarios().stream()
                .map(h -> new HorarioFuncionamento(
                        h.getDiaSemana(),
                        h.getAbertura(),
                        h.getFechamento()
                ))
                .toList();

        return new Restaurante(
                entity.getId(),
                entity.getNome(),
                entity.getTipoDeCozinha(),
                enderecoDomain,
                horarios,
                usuarioDomain,
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }

    public static RestauranteEntity paraEntidade(Restaurante domain) {
        if (domain == null) return null;

        RestauranteEntity entity = new RestauranteEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setTipoDeCozinha(domain.getTipoCozinha());
        entity.setDataCriacao(domain.getDataCadastro() != null ? domain.getDataCadastro() : new Date());
        entity.setDataAtualizacao(new Date());

        // Endereco
        if (domain.getEndereco() != null) {
            entity.setEndereco(criarEnderecoEntity(domain.getEndereco()));
        }

        // Usuário (Dono)
        if (domain.getUsuario() != null) {
            entity.setDono(criarUsuarioEntity(domain.getUsuario()));
        }

        // Horários
        if (domain.getHorariosFunc() != null && !domain.getHorariosFunc().isEmpty()) {
            List<HorarioFuncionamentoEntity> horariosEntities = domain.getHorariosFunc().stream()
                    .map(RestauranteMapper::criarHorarioEntity)
                    .toList();
            entity.setHorarios(horariosEntities);
        }

        return entity;
    }

    private static EnderecoEntity criarEnderecoEntity(Endereco endereco) {
        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(endereco.getId());
        entity.setRua(endereco.getRua());
        entity.setNumero(endereco.getNumero());
        entity.setCidade(endereco.getCidade());
        entity.setEstado(endereco.getEstado());
        entity.setCep(endereco.getCep());
        return entity;
    }

    private static UsuarioEntity criarUsuarioEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail());
        entity.setCpf(usuario.getCpf());
        entity.setCnpj(usuario.getCnpj());
        entity.setTelefone(usuario.getTelefone());
        entity.setAtivo(usuario.isAtivo());
        entity.setLogin(usuario.getLogin());
        entity.setSenha(usuario.getSenha());
        return entity;
    }

    private static HorarioFuncionamentoEntity criarHorarioEntity(HorarioFuncionamento horario) {
        HorarioFuncionamentoEntity entity = new HorarioFuncionamentoEntity();
        entity.setDiaSemana(horario.getDiaSemana());
        entity.setAbertura(horario.getAbertura());
        entity.setFechamento(horario.getFechamento());
        entity.setFechado(horario.isFechado());
        return entity;
    }
}
