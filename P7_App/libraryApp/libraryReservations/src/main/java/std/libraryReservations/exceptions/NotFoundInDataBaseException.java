package std.libraryReservations.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotFoundInDataBaseException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -1930063802323927017L;

    public NotFoundInDataBaseException() {
	super();
    }

    public NotFoundInDataBaseException(String message) {
	super(message);
    }

}
