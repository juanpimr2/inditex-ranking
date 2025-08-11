package io.github.juanpimr2.inditex_ranking.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTO {
    private Long id;
    private String name;
    private int salesUnits;
    private Map<String,Integer> stockBySize;
}
