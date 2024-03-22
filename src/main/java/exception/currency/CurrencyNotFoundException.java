package exception.currency;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class CurrencyNotFoundException extends ApplicationException {
    public CurrencyNotFoundException() {
        super(SC_NOT_FOUND, "Oops! Currency not found.");
    }
}
