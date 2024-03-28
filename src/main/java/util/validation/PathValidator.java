package util.validation;

import exception.FormFieldMissingException;
import exception.currency.CurrencyCodeMissingInAddressException;
import exception.currency.CurrencyPairCodeMissingInAddressException;
import org.apache.commons.lang3.StringUtils;

public class PathValidator {
    public static void validatePathWithParameters(String path) throws FormFieldMissingException {
        if (StringUtils.isBlank(path)) {
            throw new FormFieldMissingException();
        }
    }

    public static void validatePathWithCurrencyCode(String path) throws CurrencyCodeMissingInAddressException {
        if (StringUtils.isBlank(path)) {
            throw new CurrencyCodeMissingInAddressException();
        }
    }

    public static void validatePathWithCurrencyPairCode(String path) throws CurrencyPairCodeMissingInAddressException {
        if (StringUtils.isBlank(path)) {
            throw new CurrencyPairCodeMissingInAddressException();
        }
    }
}
