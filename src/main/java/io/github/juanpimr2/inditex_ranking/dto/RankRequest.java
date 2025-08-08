package io.github.juanpimr2.inditex_ranking.dto;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RankRequest {
  @NotNull private Weights weights;
  private List<Product> products;

  @Data @Builder @AllArgsConstructor @NoArgsConstructor
  public static class Weights {
    @DecimalMin("0.0") @DecimalMax("1.0") private double salesUnits;
    @DecimalMin("0.0") @DecimalMax("1.0") private double stockRatio;
  }
}
