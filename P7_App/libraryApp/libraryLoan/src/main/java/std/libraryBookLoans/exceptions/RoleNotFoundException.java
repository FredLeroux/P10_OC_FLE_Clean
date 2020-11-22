package std.libraryBookLoans.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {


	private static final long serialVersionUID = 7832879150809196592L;

	public RoleNotFoundException() {
		super();
	}

	public RoleNotFoundException(String message) {
		super(message);
	}

}
