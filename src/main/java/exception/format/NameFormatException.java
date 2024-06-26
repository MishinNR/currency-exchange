package exception.format;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class NameFormatException extends ApplicationException {
    public NameFormatException() {
        super(SC_BAD_REQUEST, "Oops! Name must contain no more than 40 characters");
    }
}
