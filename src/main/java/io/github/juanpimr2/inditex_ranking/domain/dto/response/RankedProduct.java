package io.github.juanpimr2.inditex_ranking.domain.dto.response;

import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RankedProduct {
  private ProductTO product;
  private double score;
}
