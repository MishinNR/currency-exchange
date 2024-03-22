package exception.currency;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class CurrencyPairCodeMissingInAddressException extends ApplicationException {
    public CurrencyPairCodeMissingInAddressException() {
        super(SC_BAD_REQUEST, "Oops! Currency pair code is missing in the address.");
    }
}
