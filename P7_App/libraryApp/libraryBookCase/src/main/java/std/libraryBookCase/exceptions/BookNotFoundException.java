package std.libraryBookCase.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1165647767084904330L;

    public BookNotFoundException() {
	super();
    }
}
