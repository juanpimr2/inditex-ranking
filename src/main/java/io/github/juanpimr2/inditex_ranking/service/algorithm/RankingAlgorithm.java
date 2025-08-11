package io.github.juanpimr2.inditex_ranking.service.algorithm;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import io.github.juanpimr2.inditex_ranking.dto.Weights;

import java.util.List;

/** Estrategia de ranking: calcula el score y devuelve los productos ordenados. */
public interface RankingAlgorithm {
    List<RankedProduct> calculateRanking(List<Product> products, Weights weights);
}