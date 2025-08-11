package io.github.juanpimr2.inditex_ranking.service.ranking.impl;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import io.github.juanpimr2.inditex_ranking.dto.RankRequest;
import io.github.juanpimr2.inditex_ranking.dto.Weights;
import io.github.juanpimr2.inditex_ranking.infrastructure.query.ProductGetAllProductsQuery;
import io.github.juanpimr2.inditex_ranking.service.algorithm.RankingAlgorithm;
import io.github.juanpimr2.inditex_ranking.service.ranking.RankingService;
import io.github.juanpimr2.inditex_ranking.service.ranking.validation.ProductValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceImplTest {

    @Mock private RankingAlgorithm algorithm;
    @Mock private ProductGetAllProductsQuery query;
    @Mock private ProductValidator validator;

    @InjectMocks private RankingServiceImpl service; // <-- Mockito llama al constructor
    // constructor esperado: (RankingAlgorithm, ProductGetAllProductsQuery, ProductValidator)

    private static Product p(int id, String name, int units) {
        return Product.builder()
                .id(id).name(name).salesUnits(units)
                .stockBySize(Map.of("S", 1, "M", 0, "L", 2))
                .build();
    }

    @Test
    void rank_shouldUseRepositoryWhenProductsNullOrEmpty() {
        // given: request sin products
        RankRequest req = new RankRequest();
        req.setWeights(new Weights()); // nulls -> se normalizan dentro del algoritmo

        List<Product> repoProducts = List.of(p(1, "A", 10), p(2, "B", 20));
        when(query.execute()).thenReturn(repoProducts);

        List<RankedProduct> expected = List.of(new RankedProduct(repoProducts.get(0), 0.1));
        when(algorithm.calculateRanking(repoProducts, req.getWeights())).thenReturn(expected);

        // when
        List<RankedProduct> out = service.rank(req);

        // then
        assertThat(out).isEqualTo(expected);
        verify(query, atLeast(1)).execute();
        verify(validator).validateProducts(repoProducts);
        verify(algorithm).calculateRanking(repoProducts, req.getWeights());
        verifyNoMoreInteractions(query, validator, algorithm);
    }

    @Test
    void rank_shouldUseProvidedProductsWhenNotEmpty() {
        // given: request con products
        List<Product> body = List.of(p(10, "X", 5));
        RankRequest req = new RankRequest();
        req.setProducts(body);
        req.setWeights(new Weights());

        List<RankedProduct> expected = List.of(new RankedProduct(body.get(0), 0.9));
        when(algorithm.calculateRanking(body, req.getWeights())).thenReturn(expected);

        // when
        List<RankedProduct> out = service.rank(req);

        // then
        assertThat(out).isEqualTo(expected);
        verify(query, never()).execute();                     // NO toca el repo
        verify(validator).validateProducts(body);
        verify(algorithm).calculateRanking(body, req.getWeights());
        verifyNoMoreInteractions(query, validator, algorithm);
    }

    @Test
    void rank_shouldUseEmptyRequestWhenNull() {
        // given: req = null  → service crea uno vacío y toma repo
        List<Product> repoProducts = List.of(p(1, "A", 10));
        when(query.execute()).thenReturn(repoProducts);

        List<RankedProduct> expected = List.of(new RankedProduct(repoProducts.get(0), 0.5));
        when(algorithm.calculateRanking(eq(repoProducts), any())).thenReturn(expected);

        // when
        List<RankedProduct> out = service.rank(null);

        // then
        assertThat(out).isEqualTo(expected);
        verify(query, atLeast(1)).execute();
        verify(validator).validateProducts(repoProducts);
        verify(algorithm).calculateRanking(eq(repoProducts), any());
        verifyNoMoreInteractions(query, validator, algorithm);
    }
}
