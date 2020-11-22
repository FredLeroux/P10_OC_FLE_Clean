package std.libraryBookLoans.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class BookNotAvailableException extends RuntimeException {


	private static final long serialVersionUID = -7301603133912747958L;

	public BookNotAvailableException() {
		super();
	}

	public BookNotAvailableException(String message) {
		super(message);
	}

}
