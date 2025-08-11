package io.github.juanpimr2.inditex_ranking.service.ranking;

import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import io.github.juanpimr2.inditex_ranking.dto.RankRequest;

import java.util.List;

/** Contrato para calcular el ranking de productos. */
public interface RankingService {
    List<RankedProduct> rank(RankRequest req);
}
