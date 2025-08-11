package io.github.juanpimr2.inditex_ranking.service.ranking.usecasae;

import io.github.juanpimr2.inditex_ranking.domain.algorithm.RankingAlgorithm;
import io.github.juanpimr2.inditex_ranking.domain.dto.ProductTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.WeightsTO;
import io.github.juanpimr2.inditex_ranking.domain.dto.request.RankRequest;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankResponse;
import io.github.juanpimr2.inditex_ranking.domain.dto.response.RankedProduct;
import io.github.juanpimr2.inditex_ranking.domain.model.Product;
import io.github.juanpimr2.inditex_ranking.domain.validation.ProductValidator;
import io.github.juanpimr2.inditex_ranking.infrastructure.query.ProductGetAllProductsQuery;
import io.github.juanpimr2.inditex_ranking.service.base.BaseService;
import io.github.juanpimr2.inditex_ranking.service.ranking.mapper.ProductMapper;
import io.github.juanpimr2.inditex_ranking.service.ranking.mapper.RankMapper;
import io.github.juanpimr2.inditex_ranking.service.ranking.port.RankingInputPort;
import io.github.juanpimr2.inditex_ranking.utils.constants.ServiceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingInputPortImpl extends BaseService<RankRequest, RankResponse>
        implements RankingInputPort {

    private final RankingAlgorithm algorithm;
    private final ProductGetAllProductsQuery getAllProducts;
    private final ProductValidator validator;
    private final ProductMapper productMapper;
    private final RankMapper rankMapper;

    @Override
    public RankResponse rank(RankRequest req) {
        return process(Optional.ofNullable(req).orElseGet(RankRequest::new));
    }

    /**
     * Resuelve productos (modelo de dominio), valida, y reinyecta en el request como TOs.
     */
    @Override
    protected void validate(RankRequest request) {
        // 1) Resolver productos como MODELO (dominio)
        List<Product> resolvedDomain = Optional.ofNullable(request.getProducts())
                .filter(list -> !list.isEmpty())
                .map(productMapper::toModelList)      // venían como TO -> los paso a modelo para validar
                .orElseGet(getAllProducts::execute);  // repo devuelve modelo

        // 2) Validar con reglas de dominio
        validator.validateProducts(resolvedDomain);

        // 3) Reinyectar al request como TOs (trabajaremos con TOs hacia fuera)
        request.setProducts(productMapper.toTOList(resolvedDomain));
    }

    @Override
    protected RankResponse doProcess(RankRequest request) {
        // Convertimos TOs -> modelo para el algoritmo de dominio
        List<ProductTO> itemsTO = request.getProducts();

        Optional<WeightsTO> weightsOpt = Optional.ofNullable(request.getWeightsTO());

        log.info(ServiceConstants.CALCULANDO_RANKING_PARA_PRODUCTOS_PESOS_SALES_UNITS_STOCK_RATIO,
                itemsTO.size(),
                weightsOpt.map(WeightsTO::getSalesUnits).orElse(null),
                weightsOpt.map(WeightsTO::getStockRatio).orElse(null));

        // Algoritmo de ranking (dominio) devuelve RankedProduct (modelo)
        List<RankedProduct> rankedModel = algorithm.calculateRanking(itemsTO, request.getWeightsTO());

        log.debug(ServiceConstants.RANKING_GENERADO_TOP_1,
                rankedModel.isEmpty() ? ServiceConstants.NINGUNA
                        : rankedModel.get(0).getProduct().getName());

        // MapStruct: modelo -> DTO wrapper RankResponse
        return rankMapper.toResponse(rankedModel);
    }
}
