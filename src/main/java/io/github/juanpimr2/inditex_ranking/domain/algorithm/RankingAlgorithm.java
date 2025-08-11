package io.github.juanpimr2.inditex_ranking.domain.algorithm;

import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.WeightsTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
import io.github.juanpimr2.inditex_ranking.domain.model.Product;

import java.util.List;

/** Estrategia de ranking: calcula el score y devuelve los productos ordenados. */
public interface RankingAlgorithm {
    List<RankedProduct> calculateRanking(List<ProductTO> products, WeightsTO weightsTO);
}