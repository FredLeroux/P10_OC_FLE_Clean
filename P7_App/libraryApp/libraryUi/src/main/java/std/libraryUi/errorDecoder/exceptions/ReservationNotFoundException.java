package std.libraryUi.errorDecoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ReservationNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 726903963218353005L;

    public ReservationNotFoundException() {
	super();
    }

    public ReservationNotFoundException(String message) {
	super(message);
    }

}
