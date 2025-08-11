package io.github.juanpimr2.inditex_ranking.domain.algorithm;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.model.RankedProduct;
import io.github.juanpimr2.inditex_ranking.domain.dto.Weights;

import java.util.List;

/** Estrategia de ranking: calcula el score y devuelve los productos ordenados. */
public interface RankingAlgorithm {
    List<RankedProduct> calculateRanking(List<Product> products, Weights weights);
}