package exception.format;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class RateFormatException extends ApplicationException {
    // Неправильный формат курса обмена - 400
    public RateFormatException() {
        super(SC_BAD_REQUEST, "Oops! Rate must consist of digits, be greater than zero.");
    }
}
