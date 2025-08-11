package io.github.juanpimr2.inditex_ranking.utils.constants;

public final class ServiceConstants {

    private ServiceConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String LOG_RANKING_START =
            "Calculando ranking para {} productos. Pesos: salesUnits={}, stockRatio={}";
    public static final String LOG_RANKING_TOP1 =
            "Ranking generado. Top1: {}";
}