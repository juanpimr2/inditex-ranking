package io.github.juanpimr2.inditex_ranking.domain.model;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RankedProduct {
  private Product product;
  private double score;
}
