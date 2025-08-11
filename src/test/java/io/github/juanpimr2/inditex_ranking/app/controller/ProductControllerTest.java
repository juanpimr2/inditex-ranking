package io.github.juanpimr2.inditex_ranking.app.controller;

import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.request.RankRequest;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankResponse;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.infrastructure.query.ProductGetAllProductsQuery;
import io.github.juanpimr2.inditex_ranking.infrastructure.web.ProductController;
import io.github.juanpimr2.inditex_ranking.service.ranking.port.RankingInputPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    private RankingInputPort rankingInputPort;

    @MockBean
    private ProductGetAllProductsQuery getAllProductsQuery;

    @Test
    void rank_shouldReturnOk() throws Exception {
        // Arrange: construimos el DTO de producto y el RankResponse que el puerto devolverá
        ProductTO productTO = ProductTO.builder()
                .id(1L).name("A").salesUnits(1).stockBySize(Map.of())
                .build();

        RankedProduct rankedDTO = RankedProduct.builder()
                .product(productTO)
                .score(0.95)
                .build();

        RankResponse response = RankResponse.builder()
                .ranked(List.of(rankedDTO))
                .build();

        when(rankingInputPort.rank(any(RankRequest.class)))
                .thenReturn(response);

        // Payload mínimo válido
        String json = """
            {
              "weightsTO": { "salesUnits": 0.5, "stockRatio": 0.5 }
            }
            """;

        // Act + Assert
        mockMvc.perform(post("/products/rank")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
