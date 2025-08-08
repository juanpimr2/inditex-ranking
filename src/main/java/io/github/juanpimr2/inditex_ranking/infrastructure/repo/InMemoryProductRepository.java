package io.github.juanpimr2.inditex_ranking.infrastructure.repo;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryProductRepository {
  private final List<Product> seed = List.of(
    p(1,"V-NECH BASIC SHIRT",100, Map.of("S",4,"M",9,"L",0)),
    p(2,"CONTRASTING FABRIC T-SHIRT",50, Map.of("S",35,"M",9,"L",9)),
    p(3,"RAISED PRINT T-SHIRT",80, Map.of("S",20,"M",2,"L",20)),
    p(4,"PLEATED T-SHIRT",3, Map.of("S",25,"M",30,"L",10)),
    p(5,"CONTRASTING LACE T-SHIRT",650, Map.of("S",0,"M",1,"L",0)),
    p(6,"SLOGAN T-SHIRT",20, Map.of("S",9,"M",2,"L",5))
  );

  public List<Product> findAll() { return new ArrayList<>(seed); }

  private static Product p(int id, String name, int units, Map<String,Integer> stock){
    return Product.builder().id(id).name(name).salesUnits(units).stockBySize(stock).build();
  }
}
