package io.github.juanpimr2.inditex_ranking.service.ranking.impl;

import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.model.RankedProduct;
import io.github.juanpimr2.inditex_ranking.domain.dto.RankRequest;
import io.github.juanpimr2.inditex_ranking.domain.dto.Weights;
import io.github.juanpimr2.inditex_ranking.infrastructure.query.ProductGetAllProductsQuery;
import io.github.juanpimr2.inditex_ranking.domain.algorithm.RankingAlgorithm;
import io.github.juanpimr2.inditex_ranking.service.base.BaseService;
import io.github.juanpimr2.inditex_ranking.service.ranking.RankingService;
import io.github.juanpimr2.inditex_ranking.domain.validation.ProductValidator;
import io.github.juanpimr2.inditex_ranking.utils.constants.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingServiceImpl extends BaseService<RankRequest, List<RankedProduct>>
        implements RankingService {

    private final RankingAlgorithm algorithm;
    private final ProductGetAllProductsQuery getAllProducts;
    private final ProductValidator validator;

    @Override
    public List<RankedProduct> rank(RankRequest req) {
        return process(Optional.ofNullable(req).orElseGet(RankRequest::new));
    }

    /**
     * Resolvemos la lista de productos UNA vez:
     *  - Si viene en la petición y no está vacía, se usa tal cual.
     *  - Si no, se carga del repositorio en memoria.
     * Validamos esa lista y la guardamos en el propio request para que doProcess()
     * no tenga que volver a resolver ni consultar al repositorio.
     */
    @Override
    protected void validate(RankRequest request) {
        final List<Product> resolved =
                Optional.ofNullable(request.getProducts())
                        .filter(list -> !list.isEmpty())
                        .orElseGet(getAllProducts::execute);

        validator.validateProducts(resolved);

        // Inyectamos los productos resueltos para evitar una segunda consulta.
        request.setProducts(resolved);
    }

    @Override
    protected List<RankedProduct> doProcess(RankRequest request) {
        final List<Product> items = request.getProducts(); // ya resueltos en validate()
        final Optional<Weights> weightsOpt = Optional.ofNullable(request.getWeights());

        log.info(ServiceConstants.CALCULANDO_RANKING_PARA_PRODUCTOS_PESOS_SALES_UNITS_STOCK_RATIO,
                items.size(),
                weightsOpt.map(Weights::getSalesUnits).orElse(null),
                weightsOpt.map(Weights::getStockRatio).orElse(null));

        final List<RankedProduct> ranked =
                algorithm.calculateRanking(items, request.getWeights());

        log.debug(ServiceConstants.RANKING_GENERADO_TOP_1,
                ranked.isEmpty() ? ServiceConstants.NINGUNA : ranked.get(0).getProduct().getName());


        return ranked;
    }
}
