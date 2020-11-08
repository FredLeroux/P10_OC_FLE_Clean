package std.libraryBookLoans.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LoanUnknown extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -7680107466941052530L;

	public LoanUnknown() {
		// TODO Auto-generated constructor stub
	}

	public LoanUnknown(String message) {
		super(message);
	}

}
