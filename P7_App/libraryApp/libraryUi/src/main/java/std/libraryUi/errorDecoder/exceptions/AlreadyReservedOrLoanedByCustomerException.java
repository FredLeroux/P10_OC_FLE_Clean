package std.libraryUi.errorDecoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AlreadyReservedOrLoanedByCustomerException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 3108688392871107264L;

    public AlreadyReservedOrLoanedByCustomerException() {
	super();
    }
}
