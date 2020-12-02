package std.libraryUi.errorDecoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LoanNotFoundException extends RuntimeException {


	private static final long serialVersionUID = 2535078644596962527L;

	public LoanNotFoundException() {
		super();
	}

	public LoanNotFoundException(String message) {
		super(message);
	}

}
