package exception.format;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class SignFormatException extends ApplicationException {
    // Неверный формат подписи валюты – 400
    public SignFormatException() {
        super(SC_BAD_REQUEST, "Oops! Sign must consist of no more than 3 characters.");
    }
}
