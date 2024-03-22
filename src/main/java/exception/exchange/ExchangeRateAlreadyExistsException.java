package exception.exchange;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

public class ExchangeRateAlreadyExistsException extends ApplicationException {
    public ExchangeRateAlreadyExistsException() {
        super(SC_CONFLICT, "Oops! Currency pair already exists.");
    }
}
