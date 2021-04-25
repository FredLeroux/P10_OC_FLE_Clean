package std.libraryUi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class EmptyListException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 8526239872216921751L;

    public EmptyListException() {
	super();
    }

    public EmptyListException(String message) {
	super(message);
    }

}
