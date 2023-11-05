package exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends Exception {
    protected Integer status;
    protected String message;

    public ApplicationException(Integer status, String message) {
        super();
        this.status = status;
        this.message = message;
    }
}
