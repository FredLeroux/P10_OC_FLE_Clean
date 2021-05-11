package std.libraryCustomers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Unknown customer")
public class CustomerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5920772278743498441L;

    public CustomerNotFoundException() {
	super();
    }

    public CustomerNotFoundException(String message) {
	super(message);
    }

}
