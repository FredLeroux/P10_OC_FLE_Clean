package std.libraryUi.errorDecoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AlreadyReservedByCustomerException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 3108688392871107264L;

    public AlreadyReservedByCustomerException() {
	super();
    }
}