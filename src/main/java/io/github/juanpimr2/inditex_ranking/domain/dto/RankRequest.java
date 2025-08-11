package io.github.juanpimr2.inditex_ranking.domain.dto;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Petición para calcular el ranking. " +
        "Si 'products' se omite o viene vacío, se usan los productos cargados en memoria.")
public class RankRequest {

  @Schema(description = "Pesos para la fórmula de scoring. Rango [0..1]. " +
          "No es obligatorio que sumen 1; se normalizan.")
  private Weights weights;

  @Schema(description = "Listado de productos a evaluar (opcional). " +
          "Si no se envía, se toma la lista cargada en memoria.")
  private List<Product> products;
}
