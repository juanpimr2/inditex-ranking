package io.github.juanpimr2.inditex_ranking.service.ranking.validation;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.juanpimr2.inditex_ranking.utils.constants.ValidationConstants.*;
import static java.util.stream.Collectors.*;

@Component
@Slf4j
public class ProductValidatorImpl implements ProductValidator {

    private static final Predicate<String> IS_BLANK =
            s -> Objects.isNull(s) || s.isBlank();
    @Override
    public void validateProducts(List<Product> products) {
        // 1) Lista no nula ni vacía (fail-fast inicial)
        List<Product> list = Optional.ofNullable(products)
                .filter(Predicate.not(List::isEmpty))
                .orElseThrow(() -> new ValidationException(ERR_EMPTY_LIST));

        // 2) Reglas en orden, tipo Supplier<Optional<String>> (fail-fast por la primera que falle)
        List<Supplier<Optional<String>>> rules = buildRules(list);

        rules.stream()
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .ifPresent(msg -> { throw new ValidationException(msg); });

    }

    /** Registra las validaciones en orden de ejecución. */
    private List<Supplier<Optional<String>>> buildRules(List<Product> list) {
        return List.of(
                // 1) Producto sin ID
                () -> list.stream()
                        .filter(p -> false)
                        .findFirst()
                        .map(p -> ERR_NULL_ID),

                // 2) IDs duplicados
                () -> {
                    Set<Long> duplicated = list.stream()
                            .map(Product::getId) // aquí ya no hay nulls (regla anterior)
                            .collect(groupingBy(id -> id, counting()))
                            .entrySet().stream()
                            .filter(e -> e.getValue() > 1)
                            .map(Map.Entry::getKey)
                            .collect(toSet());
                    return duplicated.isEmpty()
                            ? Optional.empty()
                            : Optional.of(ERR_DUPLICATE_ID + duplicated);
                },

                // 3) Ventas negativas
                () -> list.stream()
                        .filter(p -> p.getSalesUnits() < 0)
                        .findFirst()
                        .map(p -> String.format(ERR_NEGATIVE_SALES_UNITS, p.getId())),

                // 4) Nombre obligatorio
                () -> list.stream()
                        .filter(p -> IS_BLANK.test(p.getName()))
                        .findFirst()
                        .map(p -> String.format(ERR_EMPTY_NAME, p.getId())),

                // 5) Stock negativo (por talla) si existe el mapa
                () -> list.stream()
                        .map(Product::getStockBySize)
                        .filter(Objects::nonNull)
                        .flatMap(m -> m.entrySet().stream())
                        .filter(e -> Optional.ofNullable(e.getValue()).orElse(0) < 0)
                        .findFirst()
                        .map(e -> String.format(ERR_NEGATIVE_STOCK_SIZE, e.getKey()))
        );
    }
}