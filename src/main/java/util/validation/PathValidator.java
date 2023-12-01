package util.validation;

import exception.FormFieldMissingException;
import exception.currency.CurrencyCodeMissingInAddressException;
import exception.currency.CurrencyPairCodeMissingInAddressException;

public class PathValidator {
    private static final PathValidator INSTANCE = new PathValidator();

    public void validatePathParameters(String parameters) throws FormFieldMissingException {
        if (parameters.isBlank()) {
            throw new FormFieldMissingException();
        }
    }

    public void validatePathWithCurrencyCode(String path) throws CurrencyCodeMissingInAddressException {
        if (path.isBlank()) {
            throw new CurrencyCodeMissingInAddressException();
        }
    }

    public void validatePathWithCurrencyPairCode(String path) throws CurrencyPairCodeMissingInAddressException {
        if (path.isBlank()) {
            throw new CurrencyPairCodeMissingInAddressException();
        }
    }

    public static PathValidator getInstance() {
        return INSTANCE;
    }
}
