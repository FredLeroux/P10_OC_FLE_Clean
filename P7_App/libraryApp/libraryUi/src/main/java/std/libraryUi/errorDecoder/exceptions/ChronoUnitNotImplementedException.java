package std.libraryUi.errorDecoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
public class ChronoUnitNotImplementedException extends RuntimeException {

	private static final long serialVersionUID = 883839621342450941L;

	public ChronoUnitNotImplementedException() {
		super();
	}

	public ChronoUnitNotImplementedException(String message) {
		super(message);
	}

}
