package std.libraryUi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknowErrorException extends RuntimeException {


	private static final long serialVersionUID = 5506984911305234382L;

	public UnknowErrorException() {
		super();
	}

	public UnknowErrorException(String message) {
		super(message);
	}




}
