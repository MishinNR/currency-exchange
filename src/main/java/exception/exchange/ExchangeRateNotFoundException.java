package exception.exchange;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ExchangeRateNotFoundException extends ApplicationException {
    public ExchangeRateNotFoundException() {
        super(SC_NOT_FOUND, "Oops! Exchange rate for the pair was not found.");
    }
}
