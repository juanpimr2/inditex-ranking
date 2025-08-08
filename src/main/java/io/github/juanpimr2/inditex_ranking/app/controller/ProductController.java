package io.github.juanpimr2.inditex_ranking.app.controller;

import io.github.juanpimr2.inditex_ranking.dto.RankRequest;
import io.github.juanpimr2.inditex_ranking.dto.RankResponse;
import io.github.juanpimr2.inditex_ranking.infrastructure.repo.InMemoryProductRepository;
import io.github.juanpimr2.inditex_ranking.application.RankingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
  private final RankingService rankingService;
  private final InMemoryProductRepository repo;

  @PostMapping("/rank")
  public RankResponse rank(@Valid @RequestBody RankRequest req){
    var items = (req.getProducts()==null || req.getProducts().isEmpty())
        ? repo.findAll() : req.getProducts();
    return new RankResponse(rankingService.rank(items, req));
  }
}
