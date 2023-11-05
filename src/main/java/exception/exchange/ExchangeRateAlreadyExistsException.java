package exception.exchange;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

public class ExchangeRateAlreadyExistsException extends ApplicationException {
    // Валютная пара с таким кодом уже существует - 409
    public ExchangeRateAlreadyExistsException() {
        super(SC_CONFLICT, "Oops! Currency pair already exists.");
    }
}
