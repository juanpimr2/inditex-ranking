package io.github.juanpimr2.inditex_ranking.app.controller;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.dto.RankRequest;
import io.github.juanpimr2.inditex_ranking.domain.dto.RankResponse;
import io.github.juanpimr2.inditex_ranking.infrastructure.query.ProductGetAllProductsQuery;

import io.github.juanpimr2.inditex_ranking.service.ranking.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Products", description = "Listado y ranking de productos")
public class ProductController {

    private final RankingService rankingService;
    private final ProductGetAllProductsQuery getAllProductsQuery;

    @Operation(
            summary = "Listado de productos (datos en memoria)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado devuelto correctamente",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(getAllProductsQuery.execute());
    }

    @Operation(
            summary = "Ranking por score",
            description = """
        Calcula el ranking ponderando ventas y un indicador de stock.

        - `weights.salesUnits` y `weights.stockRatio` están en rango [0..1] y no necesitan sumar 1.
        - Si `products` se omite o viene vacío, se usa el repositorio en memoria.
        """,
            requestBody = @RequestBody(
                    required = false,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RankRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "70% ventas / 30% stock (usar datos en memoria)",
                                            value = """
                        { "weights": { "salesUnits": 0.7, "stockRatio": 0.3 } }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "100% ventas con productos enviados",
                                            value = """
                        {
                          "weights": { "salesUnits": 1.0, "stockRatio": 0.0 },
                          "products": [
                            { "id": 1, "name": "V-NECK BASIC SHIRT", "salesUnits": 100,
                              "stockBySize": {"S":4,"M":9,"L":0} },
                            { "id": 2, "name": "CONTRASTING FABRIC T-SHIRT", "salesUnits": 50,
                              "stockBySize": {"S":35,"M":9,"L":9} }
                          ]
                        }
                        """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ranking calculado",
                            content = @Content(schema = @Schema(implementation = RankResponse.class))
                    )
            }
    )
    @PostMapping(value = "/rank", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RankResponse> rank(@Valid @org.springframework.web.bind.annotation.RequestBody(required = false) RankRequest req) {
        var ranked = rankingService.rank(req);
        return ResponseEntity.ok(new RankResponse(ranked));
    }
}
