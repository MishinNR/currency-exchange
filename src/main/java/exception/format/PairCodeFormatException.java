package exception.format;

import exception.ApplicationException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class PairCodeFormatException extends ApplicationException {
    public PairCodeFormatException() {
        super(SC_BAD_REQUEST, "Oops! Currency pair code must consist of 6 uppercase letters.");
    }
}
