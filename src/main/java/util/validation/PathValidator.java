package util.validation;

import exception.FormFieldMissingException;
import exception.currency.CurrencyCodeMissingInAddressException;
import exception.currency.CurrencyPairCodeMissingInAddressException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathValidator {
    private static final PathValidator INSTANCE = new PathValidator();

    public void validatePathWithParameters(String path) throws FormFieldMissingException {
        if (StringUtils.isBlank(path)) {
            throw new FormFieldMissingException();
        }
    }

    public void validatePathWithCurrencyCode(String path) throws CurrencyCodeMissingInAddressException {
        if (StringUtils.isBlank(path)) {
            throw new CurrencyCodeMissingInAddressException();
        }
    }

    public void validatePathWithCurrencyPairCode(String path) throws CurrencyPairCodeMissingInAddressException {
        if (StringUtils.isBlank(path)) {
            throw new CurrencyPairCodeMissingInAddressException();
        }
    }

    public static PathValidator getInstance() {
        return INSTANCE;
    }
}
