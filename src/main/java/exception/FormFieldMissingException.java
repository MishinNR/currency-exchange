package exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class FormFieldMissingException extends ApplicationException {
    public FormFieldMissingException() {
        super(SC_BAD_REQUEST, "Oops! Required form field is missing.");
    }
}
