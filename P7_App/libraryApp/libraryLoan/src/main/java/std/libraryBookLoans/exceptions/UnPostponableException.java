package std.libraryBookLoans.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UnPostponableException extends RuntimeException {

    private static final long serialVersionUID = 2535078644596962527L;

    public UnPostponableException() {
	super();
    }

    public UnPostponableException(String message) {
	super(message);
    }

}
