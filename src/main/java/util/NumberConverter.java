package util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class NumberConverter {
    public static BigDecimal convertToDoublePrecision(BigDecimal num) {
        return num.setScale(2, RoundingMode.HALF_UP);
    }
}
