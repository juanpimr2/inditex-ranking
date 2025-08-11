package io.github.juanpimr2.inditex_ranking.domain.model;

import lombok.*;
import java.util.Map;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Product {
  private long id;
  private String name;
  private int salesUnits;
  private Map<String,Integer> stockBySize;
}
