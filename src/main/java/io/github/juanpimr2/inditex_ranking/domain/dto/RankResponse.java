package io.github.juanpimr2.inditex_ranking.domain.dto;

import io.github.juanpimr2.inditex_ranking.domain.model.RankedProduct;
import lombok.*;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RankResponse {
  private List<RankedProduct> ranked;
}
