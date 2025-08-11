package io.github.juanpimr2.inditex_ranking.service.algorithm;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import io.github.juanpimr2.inditex_ranking.dto.Weights;
import io.github.juanpimr2.inditex_ranking.infrastructure.scoring.DefaultScoringService;
import io.github.juanpimr2.inditex_ranking.infrastructure.sorting.MergeSortService;
import io.github.juanpimr2.inditex_ranking.service.algorithm.impl.RankingAlgorithmImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingAlgorithmImplTest {

    @Mock
    DefaultScoringService scoring;
    @Mock
    MergeSortService sorting;

    @InjectMocks
    RankingAlgorithmImpl algorithm; // ← Mockito inyecta los @Mock en el constructor

    @Test
    void score_shouldNormalizeWeightsAndSortDescending() {
        Product p1 = new Product(1L, "A", 10, Map.of("S", 1));
        Product p2 = new Product(2L, "B", 20, Map.of("S", 2));
        Weights w = new Weights(); w.setSalesUnits(0.5); w.setStockRatio(0.5);

        // stubs de colaboradores
        List<RankedProduct> scored = List.of(new RankedProduct(p1, 0.3), new RankedProduct(p2, 0.7));
        when(scoring.score(List.of(p1,p2), w)).thenReturn(scored);
        when(sorting.sortDescByScore(scored)).thenReturn(List.of(scored.get(1), scored.get(0)));

        List<RankedProduct> result = algorithm.calculateRanking(List.of(p1, p2), w);

        assertEquals(2L, result.get(0).getProduct().getId());
        verify(scoring).score(List.of(p1,p2), w);
        verify(sorting).sortDescByScore(scored);
    }
}