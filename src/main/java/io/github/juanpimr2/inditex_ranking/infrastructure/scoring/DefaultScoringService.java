package io.github.juanpimr2.inditex_ranking.infrastructure.scoring;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import io.github.juanpimr2.inditex_ranking.dto.RankRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultScoringService {
  private static double stockRatio(Product p){
    var values = p.getStockBySize().values();
    int total = values.size();
    long withStock = values.stream().filter(v -> v != null && v > 0).count();
    return total == 0 ? 0.0 : (double) withStock / (double) total;
  }

  public List<RankedProduct> score(List<Product> products, RankRequest.Weights w){
    int min = products.stream().mapToInt(Product::getSalesUnits).min().orElse(0);
    int max = products.stream().mapToInt(Product::getSalesUnits).max().orElse(0);

    return products.stream().map(p -> {
      double salesNorm = (max == min) ? 1.0 :
          (double)(p.getSalesUnits() - min) / (double)(max - min);
      double ratio = stockRatio(p);
      double score = w.getSalesUnits()*salesNorm + w.getStockRatio()*ratio;
      return new RankedProduct(p, score);
    }).toList();
  }
}
