package io.github.juanpimr2.inditex_ranking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
@Schema(description = "Pesos relativos para el cálculo del score. " +
        "Rango [0..1]. No es necesario que sumen 1; se normalizan internamente.")
public class Weights {

    @Schema(description = "Peso relativo de las ventas. 0 desactiva, 1 máxima importancia.",
            example = "0.7", minimum = "0.0", maximum = "1.0")
    @DecimalMin("0.0") @DecimalMax("1.0")
    private Double salesUnits;

    @Schema(description = "Peso relativo del indicador de stock. 0 desactiva, 1 máxima importancia.",
            example = "0.3", minimum = "0.0", maximum = "1.0")
    @DecimalMin("0.0") @DecimalMax("1.0")
    private Double stockRatio;
}
