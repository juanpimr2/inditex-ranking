package io.github.juanpimr2.inditex_ranking.domain.algorithm.impl;

import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.WeightsTO;
import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
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
    public List<RankedProduct> calculateRanking(List<ProductTO> products, WeightsTO weightsTO) {
        var scored = scoring.score(products, weightsTO);
        return sorting.sortDescByScore(scored);
    }
}