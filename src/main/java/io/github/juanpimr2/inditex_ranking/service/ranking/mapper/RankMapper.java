package io.github.juanpimr2.inditex_ranking.service.ranking.mapper;

import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankResponse;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RankMapper {

    /**
     * El algoritmo ya devuelve DTOs (RankedProduct). Aquí solo envolvemos la lista en RankResponse.
     */
    default RankResponse toResponse(List<RankedProduct> ranked) {
        return RankResponse.builder()
                .ranked(ranked)
                .build();
    }
}
