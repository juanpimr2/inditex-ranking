package io.github.juanpimr2.inditex_ranking.application;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import io.github.juanpimr2.inditex_ranking.dto.RankRequest;
import io.github.juanpimr2.inditex_ranking.infrastructure.scoring.DefaultScoringService;
import io.github.juanpimr2.inditex_ranking.infrastructure.sorting.MergeSortService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class RankingService {
  private final DefaultScoringService scoring;
  private final MergeSortService sorter;

  public List<RankedProduct> rank(List<Product> products, RankRequest req){
    var scored = scoring.score(products, req.getWeights());
    return sorter.sortDescByScore(scored);
  }
}
