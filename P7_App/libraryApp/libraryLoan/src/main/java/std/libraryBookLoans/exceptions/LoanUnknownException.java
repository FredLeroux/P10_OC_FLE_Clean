package std.libraryBookLoans.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LoanUnknownException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -7680107466941052530L;

	public LoanUnknownException() {
		super();
	}

	public LoanUnknownException(String message) {
		super(message);
	}

}
