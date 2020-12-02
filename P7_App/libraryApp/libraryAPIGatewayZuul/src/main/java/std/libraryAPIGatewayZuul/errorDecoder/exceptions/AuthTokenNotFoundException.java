package std.libraryAPIGatewayZuul.errorDecoder.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AuthTokenNotFoundException extends RuntimeException {


	private static final long serialVersionUID = -2963799829394963281L;

	public AuthTokenNotFoundException() {
		super();
	}

	public AuthTokenNotFoundException(String message) {
		super(message);
	}

}
