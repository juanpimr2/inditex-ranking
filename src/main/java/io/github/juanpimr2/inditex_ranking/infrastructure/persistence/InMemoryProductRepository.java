package io.github.juanpimr2.inditex_ranking.infrastructure.persistence;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryProductRepository {
  private final List<Product> seed = List.of(
    p(1L,"V-NECH BASIC SHIRT",100, Map.of("S",4,"M",9,"L",0)),
    p(2L,"CONTRASTING FABRIC T-SHIRT",50, Map.of("S",35,"M",9,"L",9)),
    p(3L,"RAISED PRINT T-SHIRT",80, Map.of("S",20,"M",2,"L",20)),
    p(4L,"PLEATED T-SHIRT",3, Map.of("S",25,"M",30,"L",10)),
    p(5L,"CONTRASTING LACE T-SHIRT",650, Map.of("S",0,"M",1,"L",0)),
    p(6L,"SLOGAN T-SHIRT",20, Map.of("S",9,"M",2,"L",5))
  );

  public List<Product> findAll() { return new ArrayList<>(seed); }

  private static Product p(Long id, String name, int units, Map<String,Integer> stock){
    return Product.builder().id(id).name(name).salesUnits(units).stockBySize(stock).build();
  }
}
