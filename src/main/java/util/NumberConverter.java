package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberConverter {
    private static final int SCALE = 2;

    public static BigDecimal convertToDoublePrecision(BigDecimal num) {
        return num.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
