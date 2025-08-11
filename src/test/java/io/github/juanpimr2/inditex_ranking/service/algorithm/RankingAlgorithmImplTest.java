package io.github.juanpimr2.inditex_ranking.service.algorithm;

import io.github.juanpimr2.inditex_ranking.domain.algorithm.impl.RankingAlgorithmImpl;
import io.github.juanpimr2.inditex_ranking.domain.algorithm.scoring.DefaultScoringService;
import io.github.juanpimr2.inditex_ranking.domain.algorithm.sorting.MergeSortService;
import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.WeightsTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    private static ProductTO to(Long id, String name, int units) {
        ProductTO p = new ProductTO();
        p.setId(id);
        p.setName(name);
        p.setSalesUnits(units);
        p.setStockBySize(Map.of("S", 1, "M", 0, "L", 2));
        return p;
    }

    @Test
    void score_shouldNormalizeWeightsAndSortDescending() {
        ProductTO p1 = to(10L, "X", 5);
        ProductTO p2 = to(2L, "B", 20);
        WeightsTO w = new WeightsTO(); w.setSalesUnits(0.5); w.setStockRatio(0.5);

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