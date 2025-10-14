package pos.tech.cleanarchandjpa.infra.database.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pos.tech.cleanarchandjpa.core.domain.Cardapio;
import pos.tech.cleanarchandjpa.core.domain.Restaurante;
import pos.tech.cleanarchandjpa.core.domain.Usuario;
import pos.tech.cleanarchandjpa.core.dto.cardapio.CardapioResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.paginacao.PaginacaoResult;
import pos.tech.cleanarchandjpa.core.dto.paginacao.ParametrosPag;
import pos.tech.cleanarchandjpa.core.dto.restaurante.RestauranteResponseDTO;
import pos.tech.cleanarchandjpa.core.dto.usuario.UsuarioResponseDTO;
import pos.tech.cleanarchandjpa.infra.database.entity.CardapioEntity;
import pos.tech.cleanarchandjpa.infra.database.entity.RestauranteEntity;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginacaoMapper {


    public static PaginacaoResult<UsuarioResponseDTO> fromDto(PaginacaoResult<Usuario> result) {
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

    public static PaginacaoResult<RestauranteResponseDTO> fromDtoR(PaginacaoResult<Restaurante> result) {
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

    public static PaginacaoResult<CardapioResponseDTO> fromDtoCardapio(PaginacaoResult<Cardapio> result) {
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

    public static PaginacaoResult<Restaurante> fromEntityPage(Page<RestauranteEntity> page) {
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

    public static PaginacaoResult<Cardapio> fromEntityPageCardapio(Page<CardapioEntity> page) {
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


    public static ParametrosPag fromPageable(Pageable pageable) {
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

    public static Pageable toPageable(ParametrosPag parametrosPag) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(parametrosPag.sortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(
                parametrosPag.page(),
                parametrosPag.pageSize(),
                Sort.by(direction, parametrosPag.sortBy())
        );
    }
}
