package exception.format;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class AmountFormatException extends ApplicationException {
    // Неправильный формат суммы - 400
    public AmountFormatException() {
        super(SC_BAD_REQUEST, "Oops! Amount must consist of digits, be greater than zero.");
    }
}
