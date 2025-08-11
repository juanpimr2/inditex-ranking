package io.github.juanpimr2.inditex_ranking.service.ranking.usecasae.RankingServiceImplTest;

import io.github.juanpimr2.inditex_ranking.domain.algorithm.RankingAlgorithm;
import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.WeightsTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.request.RankRequest;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankResponse;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.validation.ProductValidator;
import io.github.juanpimr2.inditex_ranking.infrastructure.query.ProductGetAllProductsQuery;
import io.github.juanpimr2.inditex_ranking.service.ranking.mapper.ProductMapper;
import io.github.juanpimr2.inditex_ranking.service.ranking.mapper.RankMapper;
import io.github.juanpimr2.inditex_ranking.service.ranking.usecasae.RankingInputPortImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingInputPortImplTest {

    // Mocks de dependencias
    @Mock private RankingAlgorithm algorithm;
    @Mock private ProductGetAllProductsQuery query;
    @Mock private ProductValidator validator;

    // Mappers reales (MapStruct) como @Spy
    @Spy private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
    @Spy private RankMapper rankMapper = Mappers.getMapper(RankMapper.class);

    // SUT
    @InjectMocks
    private RankingInputPortImpl service;

    // ---------- helpers ----------
    private static Product model(Long id, String name, int units) {
        return Product.builder()
                .id(id)
                .name(name)
                .salesUnits(units)
                .stockBySize(Map.of("S", 1, "M", 0, "L", 2))
                .build();
    }

    private static ProductTO to(Long id, String name, int units) {
        ProductTO p = new ProductTO();
        p.setId(id);
        p.setName(name);
        p.setSalesUnits(units);
        p.setStockBySize(Map.of("S", 1, "M", 0, "L", 2));
        return p;
    }

    private static RankedProduct ranked(ProductTO p, double score) {
        return RankedProduct.builder().product(p).score(score).build();
    }

    // ============ TESTS ============

    @Test
    void rank_shouldUseRepositoryWhenProductsNullOrEmpty() {
        // given: request sin productos
        RankRequest req = new RankRequest();
        req.setWeightsTO(new WeightsTO()); // valores por defecto (se normalizan en el algoritmo)

        // repo devuelve modelos de dominio
        List<Product> repo = List.of(model(1L, "A", 10), model(2L, "B", 20));
        when(query.execute()).thenReturn(repo);

        // algoritmo devuelve lista de RankedProduct (DTO)
        List<ProductTO> expectedTOs = productMapper.toTOList(repo);
        List<RankedProduct> rankedList = List.of(
                ranked(expectedTOs.get(0), 0.42),
                ranked(expectedTOs.get(1), 0.33)
        );
        when(algorithm.calculateRanking(anyList(), any())).thenReturn(rankedList);

        // when
        RankResponse response = service.rank(req);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getRanked()).usingRecursiveComparison().isEqualTo(rankedList);

        // validó modelos del repo
        verify(validator).validateProducts(repo);
        // tiró del repo
        verify(query, times(1)).execute();

        // al algoritmo le pasamos TOs (no modelos)
        ArgumentCaptor<List<ProductTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(algorithm).calculateRanking(captor.capture(), eq(req.getWeightsTO()));
        List<ProductTO> passedToAlgo = captor.getValue();
        assertThat(passedToAlgo).usingRecursiveComparison().isEqualTo(expectedTOs);

        verifyNoMoreInteractions(query, validator, algorithm);
    }

    @Test
    void rank_shouldUseProvidedProductsWhenNotEmpty() {
        // given: request con productos en DTO
        List<ProductTO> bodyTO = List.of(to(10L, "X", 5));
        RankRequest req = new RankRequest();
        req.setProducts(bodyTO);
        req.setWeightsTO(new WeightsTO());

        // esperado: validator recibe MODELOS mapeados desde el body
        List<Product> expectedModels = productMapper.toModelList(bodyTO);

        // y el algoritmo recibe TOs exactamente como en el body
        List<RankedProduct> rankedList = List.of(ranked(bodyTO.get(0), 0.9));
        when(algorithm.calculateRanking(anyList(), any())).thenReturn(rankedList);

        // when
        RankResponse response = service.rank(req);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getRanked()).usingRecursiveComparison().isEqualTo(rankedList);

        // no toca el repo
        verify(query, never()).execute();
        // validación sobre MODELOS
        verify(validator).validateProducts(expectedModels);

        // el algoritmo recibe los TO del body
        ArgumentCaptor<List<ProductTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(algorithm).calculateRanking(captor.capture(), eq(req.getWeightsTO()));
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(bodyTO);

        verifyNoMoreInteractions(query, validator, algorithm);
    }

    @Test
    void rank_shouldCreateEmptyRequestWhenNullAndUseRepo() {
        // given: req = null → service crea uno vacío y usa repo
        List<Product> repo = List.of(model(1L, "A", 10));
        when(query.execute()).thenReturn(repo);

        List<ProductTO> expectedTOs = productMapper.toTOList(repo);
        List<RankedProduct> rankedList = List.of(ranked(expectedTOs.get(0), 0.5));
        when(algorithm.calculateRanking(anyList(), any())).thenReturn(rankedList);

        // when
        RankResponse response = service.rank(null);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getRanked()).usingRecursiveComparison().isEqualTo(rankedList);

        verify(query, times(1)).execute();
        verify(validator).validateProducts(repo);

        ArgumentCaptor<List<ProductTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(algorithm).calculateRanking(captor.capture(), any());
        assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(expectedTOs);

        verifyNoMoreInteractions(query, validator, algorithm);
    }
}
