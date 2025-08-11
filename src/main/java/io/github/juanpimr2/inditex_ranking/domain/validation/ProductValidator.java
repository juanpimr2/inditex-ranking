package io.github.juanpimr2.inditex_ranking.domain.validation;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;

import java.util.List;

public interface ProductValidator {
    void validateProducts(List<Product> products);
}