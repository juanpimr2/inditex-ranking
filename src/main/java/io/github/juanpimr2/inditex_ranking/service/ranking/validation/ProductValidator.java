package io.github.juanpimr2.inditex_ranking.service.ranking.validation;

import io.github.juanpimr2.inditex_ranking.domain.Product;

import java.util.List;

public interface ProductValidator {
    void validateProducts(List<Product> products);
}