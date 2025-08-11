package io.github.juanpimr2.inditex_ranking.domain.dto.response;

import lombok.*;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RankResponse {
  private List<RankedProduct> ranked;
}
