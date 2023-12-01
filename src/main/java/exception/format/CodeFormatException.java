package exception.format;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class CodeFormatException extends ApplicationException {
    // Неправильный формат кода валют – 400
    public CodeFormatException() {
        super(SC_BAD_REQUEST, "Oops! Currency code must consist of 3 uppercase letters.");
    }
}
