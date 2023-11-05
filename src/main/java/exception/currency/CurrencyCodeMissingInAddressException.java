package exception.currency;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class CurrencyCodeMissingInAddressException extends ApplicationException {
    // Код валюты отсутствует в адресе - 400
    public CurrencyCodeMissingInAddressException() {
        super(SC_BAD_REQUEST, "Oops! Currency code is missing in the address.");
    }
}
