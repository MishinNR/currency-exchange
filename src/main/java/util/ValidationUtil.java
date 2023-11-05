package util;

import exception.FormFieldMissingException;
import exception.currency.CurrencyCodeMissingInAddressException;
import exception.format.*;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class ValidationUtil {
    public static void validateRequestParameterIsPresent(String param) throws FormFieldMissingException {
        if (param == null || param.isBlank()) {
            throw new FormFieldMissingException();
        }
    }

    public static void validateRequestParametersArePresent(String param1, String param2, String param3) throws FormFieldMissingException {
        checkRequestParametersNotNull(param1, param2, param3);
        checkRequestParametersNotBlank(param1, param2, param3);
    }

    private static void checkRequestParametersNotNull(String param1, String param2, String param3) throws FormFieldMissingException {
        if (param1 == null || param2 == null || param3 == null) {
            throw new FormFieldMissingException();
        }
    }

    private static void checkRequestParametersNotBlank(String param1, String param2, String param3) throws FormFieldMissingException {
        if (param1.isBlank() || param2.isBlank() || param3.isBlank()) {
            throw new FormFieldMissingException();
        }
    }

    public static void validateCurrencyCodeExistsInAddress(String path) throws CurrencyCodeMissingInAddressException {
        if (path == null) {
            throw new CurrencyCodeMissingInAddressException();
        }
    }

    public static void validateParameterCode(String code) throws CodeFormatException {
        if (code.trim().length() != 3 || !code.chars().allMatch(Character::isLetter)) {
            throw new CodeFormatException();
        }
    }

    public static void validateParameterPairCode(String pairCode) throws PairCodeFormatException {
        if (pairCode.trim().length() != 6 || !pairCode.chars().allMatch(Character::isLetter)) {
            throw new PairCodeFormatException();
        }
    }

    public static void validateParameterRate(String rate) throws RateFormatException {
        try {
            BigDecimal num = new BigDecimal(rate);
            if (num.compareTo(ZERO) <= 0) {
                throw new RateFormatException();
            }
        } catch (NumberFormatException e) {
            throw new RateFormatException();
        }
    }

    public static void validateParameterAmount(String amount) throws AmountFormatException {
        try {
            BigDecimal num = new BigDecimal(amount);
            if (num.compareTo(ZERO) <= 0) {
                throw new AmountFormatException();
            }
        } catch (NumberFormatException e) {
            throw new AmountFormatException();
        }
    }

    public static void validateParameterName(String name) throws NameFormatException {
        if (name.trim().length() > 40) {
            throw new NameFormatException();
        }
    }

    public static void validateParameterSign(String sign) throws SignFormatException {
        if (sign.trim().length() > 3) {
            throw new SignFormatException();
        }
    }
}
