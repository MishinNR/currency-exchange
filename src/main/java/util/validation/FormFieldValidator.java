package util.validation;

import exception.format.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class FormFieldValidator {
    private static final FormFieldValidator INSTANCE = new FormFieldValidator();

    public void validateName(String name) throws NameFormatException {
        if (StringUtils.isBlank(name)) {
            throw new NameFormatException();
        } else if (name.trim().length() > 40) {
            throw new NameFormatException();
        }
    }

    public void validateCode(String code) throws CodeFormatException {
        if (StringUtils.isBlank(code)) {
            throw new CodeFormatException();
        } else if (code.trim().length() != 3
                   || !code.chars().allMatch(Character::isLetter)
                   || !code.equals(code.toUpperCase())
        ) {
            throw new CodeFormatException();
        }
    }

    public void validatePairCode(String pairCode) throws PairCodeFormatException {
        if (StringUtils.isBlank(pairCode)) {
            throw new PairCodeFormatException();
        } else if (pairCode.trim().length() != 6
                   || !pairCode.chars().allMatch(Character::isLetter)
                   || !pairCode.equals(pairCode.toUpperCase())) {
            throw new PairCodeFormatException();
        }
    }

    public void validateSign(String sign) throws SignFormatException {
        if (StringUtils.isBlank(sign)) {
            throw new SignFormatException();
        } else if (sign.trim().length() > 3) {
            throw new SignFormatException();
        }
    }

    public void validateRate(String rate) throws RateFormatException {
        try {
            if (StringUtils.isBlank(rate)) {
                throw new RateFormatException();
            }
            BigDecimal num = new BigDecimal(rate);
            if (num.compareTo(ZERO) <= 0) {
                throw new RateFormatException();
            }
        } catch (NumberFormatException e) {
            throw new RateFormatException();
        }
    }

    public void validateAmount(String amount) throws AmountFormatException {
        try {
            if (StringUtils.isBlank(amount)) {
                throw new AmountFormatException();
            }
            BigDecimal num = new BigDecimal(amount);
            if (num.compareTo(ZERO) <= 0) {
                throw new AmountFormatException();
            }
        } catch (NumberFormatException e) {
            throw new AmountFormatException();
        }
    }

    public static FormFieldValidator getInstance() {
        return INSTANCE;
    }
}
