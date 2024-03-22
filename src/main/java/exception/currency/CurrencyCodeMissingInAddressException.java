package exception.currency;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class CurrencyCodeMissingInAddressException extends ApplicationException {
    public CurrencyCodeMissingInAddressException() {
        super(SC_BAD_REQUEST, "Oops! Currency code is missing in the address.");
    }
}
