package io.github.juanpimr2.inditex_ranking.service.validation;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.service.ranking.validation.ProductValidatorImpl;
import io.github.juanpimr2.inditex_ranking.service.ranking.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidatorImplTest {

    private ProductValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new ProductValidatorImpl();
    }

    @Test
    void validate_shouldThrowWhenListIsNullOrEmpty() {
        assertThrows(ValidationException.class, () -> validator.validateProducts(null));
        assertThrows(ValidationException.class, () -> validator.validateProducts(List.of()));
    }

    @Test
    void validate_shouldThrowOnDuplicateIds() {
        Product p1 = new Product(1, "A", 10, Map.of("S", 1));
        Product p2 = new Product(1, "B", 15, Map.of("S", 2));
        assertThrows(ValidationException.class, () -> validator.validateProducts(List.of(p1, p2)));
    }

    @Test
    void validate_shouldThrowOnNegativeSales() {
        Product p1 = new Product(1, "A", -1, Map.of("S", 1));
        assertThrows(ValidationException.class, () -> validator.validateProducts(List.of(p1)));
    }

    @Test
    void validate_shouldThrowOnBlankName() {
        Product p1 = new Product(1, " ", 1, Map.of("S", 1));
        assertThrows(ValidationException.class, () -> validator.validateProducts(List.of(p1)));
    }

    @Test
    void validate_shouldPassForValidProducts() {
        Product p1 = new Product(1, "A", 10, Map.of("S", 1));
        assertDoesNotThrow(() -> validator.validateProducts(List.of(p1)));
    }
}