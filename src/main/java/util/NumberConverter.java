package util;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class NumberConverter {
    public static BigDecimal convertToDoublePrecision(BigDecimal num) {
        return num.setScale(2, RoundingMode.HALF_UP);
    }
}
