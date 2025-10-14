package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.domain.TipoUsuario;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.tipousuario.TipoUsuarioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioResponseDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.CardapioEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.RestauranteEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.TipoUsuarioEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.UsuarioEntity;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginacaoMapper {


    public static PaginacaoResult<UsuarioResponseDTO> paraResponsePaginacao(PaginacaoResult<Usuario> result) {
        List<UsuarioResponseDTO> dtos = result.content().stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail()))
                .toList();

        return new PaginacaoResult<>(
                dtos,
                result.page(),
                result.pageSize(),
                result.totalElements(),
                result.totalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    public static PaginacaoResult<RestauranteResponseDTO> paraResponsePaginacaoRestaurante(PaginacaoResult<Restaurante> result) {
        List<RestauranteResponseDTO> dtos = result.content().stream()
                .map(u -> new RestauranteResponseDTO(u.getId(), u.getNome(), u.getUsuario().getId()))
                .toList();
        return new PaginacaoResult<>(
                dtos,
                result.page(),
                result.pageSize(),
                result.totalElements(),
                result.totalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    public static PaginacaoResult<CardapioResponseDTO> paraResponsePaginacaoCardapio(PaginacaoResult<Cardapio> result) {
        List<CardapioResponseDTO> dtos = result.content().stream()
                .map(u -> new CardapioResponseDTO(u.getId(), u.getNome(), u.getDescricao()))
                .toList();
        return new PaginacaoResult<>(
                dtos,
                result.page(),
                result.pageSize(),
                result.totalElements(),
                result.totalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    public static PaginacaoResult<Restaurante> dePageParaPaginacaoRestaurante(Page<RestauranteEntity> page) {
        List<Restaurante> restaurantes = page.getContent()
                .stream()
                .map(RestauranteMapper::paraDominio)
                .toList();

        return new PaginacaoResult<>(
                restaurantes,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    public static PaginacaoResult<Cardapio> dePageParaPaginacaoCardapio(Page<CardapioEntity> page) {
        List<Cardapio> cardapios = page.getContent()
                .stream()
                .map(CardapioMapper::paraDominioDeEntidade)
                .toList();

        return new PaginacaoResult<>(
                cardapios,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }


    public static ParametrosPag dePageableParaParametrosPag(Pageable pageable) {
        String sortDirection = pageable.getSort().isEmpty()
                ? "ASC"
                : pageable.getSort().stream()
                .findFirst()
                .map(order -> order.getDirection().toString())
                .orElse("ASC");

        String sortBy = pageable.getSort().isEmpty()
                ? "id"
                : pageable.getSort().stream()
                .findFirst()
                .map(Sort.Order::getProperty)
                .orElse("id");

        return new ParametrosPag(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortBy,
                sortDirection
        );
    }

    public static Pageable deParametrosPagParaPageable(ParametrosPag parametrosPag) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(parametrosPag.sortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(
                parametrosPag.page(),
                parametrosPag.pageSize(),
                Sort.by(direction, parametrosPag.sortBy())
        );
    }

    public static PaginacaoResult<TipoUsuario> dePageParaPaginacaoTipoUsuario(Page<TipoUsuarioEntity> page) {
        List<TipoUsuario> tipoUsuarios = page.getContent()
                .stream()
                .map(TipoUsuarioMapper::paraDominio)
                .toList();

        return new PaginacaoResult<>(
                tipoUsuarios,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    public static PaginacaoResult<TipoUsuarioResponseDTO> paraResponsePaginacaoTipoUsuario(PaginacaoResult<TipoUsuario> usuarios) {
        List<TipoUsuarioResponseDTO> dtos = usuarios.content().stream()
                .map(u -> new TipoUsuarioResponseDTO(u.getId(), u.getNomeTipoUsuario(), u.getUsuario()))
                .toList();
        return new PaginacaoResult<>(
                dtos,
                usuarios.page(),
                usuarios.pageSize(),
                usuarios.totalElements(),
                usuarios.totalPages(),
                usuarios.hasNext(),
                usuarios.hasPrevious()
        );
    }

    public static PaginacaoResult<Usuario> dePageParaPaginacaoUsuario(Page<UsuarioEntity> page) {
        List<Usuario> usuarios = page.getContent()
                .stream()
                .map(UsuarioMapper::paraDominio)
                .toList();
        return new PaginacaoResult<>(
                usuarios,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
