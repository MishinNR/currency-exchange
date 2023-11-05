package exception;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class DatabaseException extends ApplicationException {
    // Ошибка (например, база данных недоступна) - 500
    public DatabaseException() {
        super(SC_INTERNAL_SERVER_ERROR, "Oops! Something happened with the database.");
    }
}
