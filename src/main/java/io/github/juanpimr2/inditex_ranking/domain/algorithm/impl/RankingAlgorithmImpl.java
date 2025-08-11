package io.github.juanpimr2.inditex_ranking.domain.algorithm.impl;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.model.RankedProduct;
import io.github.juanpimr2.inditex_ranking.domain.dto.Weights;
import io.github.juanpimr2.inditex_ranking.domain.algorithm.scoring.DefaultScoringService;
import io.github.juanpimr2.inditex_ranking.domain.algorithm.sorting.MergeSortService;
import io.github.juanpimr2.inditex_ranking.domain.algorithm.RankingAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Algoritmo por defecto: score (O(n)) + ordenación (O(n log n)) estable.
 * Mantiene servicios desacoplados y fácilmente testeables por separado.
 */
@Service
@Primary
@RequiredArgsConstructor
public class RankingAlgorithmImpl implements RankingAlgorithm {

    private final DefaultScoringService scoring;
    private final MergeSortService sorting;

    @Override
    public List<RankedProduct> calculateRanking(List<Product> products, Weights weights) {
        var scored = scoring.score(products, weights);
        return sorting.sortDescByScore(scored);
    }
}