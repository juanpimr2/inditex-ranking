package io.github.juanpimr2.inditex_ranking.app.controller;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.model.RankedProduct;
import io.github.juanpimr2.inditex_ranking.infrastructure.query.ProductGetAllProductsQuery;
import io.github.juanpimr2.inditex_ranking.service.ranking.RankingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankingService rankingService;

    @MockBean
    private ProductGetAllProductsQuery getAllProductsQuery;

    @Test
    void list_shouldReturnOk() throws Exception {
        when(getAllProductsQuery.execute()).thenReturn(List.of(new Product(1L, "A", 1, Map.of())));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    void rank_shouldReturnOk() throws Exception {
        when(rankingService.rank(any())).thenReturn(List.of(new RankedProduct(new Product(1, "A", 1, Map.of()), 1.0)));

        String json = "{\"weights\":{\"salesUnits\":0.5,\"stockRatio\":0.5}}";
        mockMvc.perform(post("/products/rank")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }
}