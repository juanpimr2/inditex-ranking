package io.github.juanpimr2.inditex_ranking.service.ranking.mapper;

import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    // Elemento
    ProductTO toTO(Product source);
    Product toModel(ProductTO source);

    // Listas (MapStruct las genera automáticamente a partir de los métodos de elemento)
    List<ProductTO> toTOList(List<Product> source);
    List<Product> toModelList(List<ProductTO> source);
}