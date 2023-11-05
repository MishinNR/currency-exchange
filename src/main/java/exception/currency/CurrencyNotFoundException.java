package exception.currency;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class CurrencyNotFoundException extends ApplicationException {
    // Валюта не найдена - 404
    public CurrencyNotFoundException() {
        super(SC_NOT_FOUND, "Oops! Currency not found.");
    }
}
