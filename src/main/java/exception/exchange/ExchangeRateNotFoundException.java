package exception.exchange;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ExchangeRateNotFoundException extends ApplicationException {
    // Обменный курс для пары не найден - 404
    public ExchangeRateNotFoundException() {
        super(SC_NOT_FOUND, "Oops! Exchange rate for the pair was not found.");
    }
}
