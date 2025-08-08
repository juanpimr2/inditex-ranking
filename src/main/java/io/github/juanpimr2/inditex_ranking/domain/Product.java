package io.github.juanpimr2.inditex_ranking.domain;

import lombok.*;
import java.util.Map;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Product {
  private int id;
  private String name;
  private int salesUnits;
  private Map<String,Integer> stockBySize;
}
