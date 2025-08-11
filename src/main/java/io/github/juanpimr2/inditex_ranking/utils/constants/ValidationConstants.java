package io.github.juanpimr2.inditex_ranking.utils.constants;

public final class ValidationConstants {

    private ValidationConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ERR_EMPTY_LIST = "La lista de productos no puede estar vacía.";
    public static final String ERR_NULL_ID = "Encontrado producto sin 'id'.";
    public static final String ERR_DUPLICATE_ID = "ID(s) de producto duplicado(s): ";
    public static final String ERR_NEGATIVE_SALES_UNITS = "salesUnits no puede ser negativo (id=%d).";
    public static final String ERR_EMPTY_NAME = "name es obligatorio (id=%d).";
    public static final String LOG_MISSING_STOCK_BY_SIZE = "Producto id={} sin stockBySize, se asume {{}}.";
    public static final String ERR_NEGATIVE_STOCK_SIZE = "Stock negativo para talla '%s'.";
}