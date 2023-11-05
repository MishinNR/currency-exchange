package exception.currency;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

public class CurrencyAlreadyExistsException extends ApplicationException {
    // Валюта с таким кодом уже существует - 409
    public CurrencyAlreadyExistsException() {
        super(SC_CONFLICT, "Oops! Currency already exists.");
    }
}
