package exception;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class FormFieldMissingException extends ApplicationException {
    // Отсутствует нужное поле формы - 400
    public FormFieldMissingException() {
        super(SC_BAD_REQUEST, "Oops! Required form field is missing.");
    }
}
