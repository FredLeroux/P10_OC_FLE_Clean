package std.libraryBookLoans.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BuildingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7008462796373694179L;

	public BuildingNotFoundException() {
		super();
	}

	public BuildingNotFoundException(String message) {
		super(message);
	}

}
