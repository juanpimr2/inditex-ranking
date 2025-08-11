package io.github.juanpimr2.inditex_ranking.infrastructure.query;


import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.infrastructure.repo.InMemoryProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ProductGetAllProductsQuery {
    private final InMemoryProductRepository repo;

    public List<Product> execute() {
        return repo.findAll();
    }
}
