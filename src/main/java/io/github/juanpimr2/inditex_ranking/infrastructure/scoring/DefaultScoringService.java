package io.github.juanpimr2.inditex_ranking.infrastructure.scoring;

import io.github.juanpimr2.inditex_ranking.domain.Product;
import io.github.juanpimr2.inditex_ranking.domain.RankedProduct;
import io.github.juanpimr2.inditex_ranking.dto.Weights;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Servicio de cálculo de puntuaciones (Scoring) para productos.
 *
 * <p>Este servicio combina dos métricas:</p>
 * <ul>
 *   <li><strong>Ventas</strong> (salesUnits) → normalizadas usando min-max scaling.</li>
 *   <li><strong>Ratio de stock</strong> → proporción de tallas con stock disponible.</li>
 * </ul>
 *
 * <p>Los pesos de ambas métricas se normalizan internamente.
 * Si uno de los pesos es cero, la métrica correspondiente se ignora totalmente.</p>
 *
 * <p>Complejidad: O(n) en todos los casos.</p>
 */
@Service
public class DefaultScoringService {

  private static final double DEFAULT_SALES_WEIGHT = 1.0;
  private static final double DEFAULT_STOCK_WEIGHT = 0.0;
  private static final double MIN_RATIO = 0.0;
  private static final double MAX_RATIO = 1.0;

  /**
   * Calcula el ratio de tallas con stock disponible para un producto.
   */
  private static double stockRatio(Product product) {
    if (Objects.isNull(product.getStockBySize()) || product.getStockBySize().isEmpty()) {
      return MIN_RATIO;
    }
    var values = product.getStockBySize().values();
    long withStock = values.stream()
            .filter(Objects::nonNull)
            .filter(v -> v > 0)
            .count();
    return values.isEmpty() ? MIN_RATIO : (double) withStock / values.size();
  }

  /**
   * Calcula el score para cada producto usando las métricas y pesos indicados.
   *
   * @param products lista de productos
   * @param weights  configuración de pesos (opcional)
   * @return lista de productos con su puntuación calculada
   */
  public List<RankedProduct> score(List<Product> products, Weights weights) {

    // 1) Pesos con defaults y normalización
    double wSales = Optional.ofNullable(weights)
            .map(Weights::getSalesUnits)
            .orElse(DEFAULT_SALES_WEIGHT);

    double wStock = Optional.ofNullable(weights)
            .map(Weights::getStockRatio)
            .orElse(DEFAULT_STOCK_WEIGHT);

    // Si ambos pesos son cero, usar solo ventas como fallback
    if (wSales <= 0.0 && wStock <= 0.0) {
      wSales = DEFAULT_SALES_WEIGHT;
      wStock = DEFAULT_STOCK_WEIGHT;
    }

    double sum = wSales + wStock;
    wSales /= sum;
    wStock /= sum;

    // 2) Normalización de ventas
    int minSales = products.stream().mapToInt(Product::getSalesUnits).min().orElse(0);
    int maxSales = products.stream().mapToInt(Product::getSalesUnits).max().orElse(0);

    final double finalWSales = wSales;
    final double finalWStock = wStock;

    // 3) Calcular score combinado
    return products.stream()
            .map(product -> {
              double salesNorm = (maxSales == minSales)
                      ? MAX_RATIO
                      : (double) (product.getSalesUnits() - minSales) / (maxSales - minSales);

              double ratio = stockRatio(product);

              double score = (finalWSales > 0 ? finalWSales * salesNorm : 0)
                      + (finalWStock > 0 ? finalWStock * ratio : 0);

              return new RankedProduct(product, score);
            })
            .toList();
  }
}
