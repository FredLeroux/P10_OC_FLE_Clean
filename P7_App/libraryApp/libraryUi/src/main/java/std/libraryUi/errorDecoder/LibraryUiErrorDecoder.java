package std.libraryUi.errorDecoder;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;
import std.libraryUi.errorDecoder.exceptions.AlreadyReservedOrLoanedByCustomerException;
import std.libraryUi.errorDecoder.exceptions.BookNotAvailableException;
import std.libraryUi.errorDecoder.exceptions.ChronoUnitNotImplementedException;
import std.libraryUi.errorDecoder.exceptions.LoanNotFoundException;
import std.libraryUi.errorDecoder.exceptions.NotFoundException;
import std.libraryUi.errorDecoder.exceptions.NotFoundInDataBaseException;
import std.libraryUi.errorDecoder.exceptions.ReservationNotFoundException;
import std.libraryUi.errorDecoder.exceptions.UnPostponableException;
import std.libraryUi.exceptions.UnknowErrorException;;

@Component
public class LibraryUiErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
	if (response.status() == 404) {
	    if (methodKey.contains("postpone")) {
		return new LoanNotFoundException();
	    }

	}
	if (response.status() == 501) {
	    if (methodKey.contains("postpone")) {
		return new ChronoUnitNotImplementedException();
	    }
	}

	if (response.status() == 409) {
	    if (methodKey.contains("postpone")) {
		return new UnPostponableException("Loan can't be postponed cause late");
	    }
	}
	if (response.status() == 409) {
	    if (methodKey.contains("createReservation")) {
		return new AlreadyReservedOrLoanedByCustomerException();
	    }
	}

	if (response.status() == 409) {
	    if (methodKey.contains("createLoan")) {
		return new BookNotAvailableException();
	    }
	}

	if (response.status() == 404) {
	    if (methodKey.contains("createLoanFromReservation")) {
		return new ReservationNotFoundException("Reservation service reservation not found");
	    }
	}

	if (response.status() == 404) {
	    if (methodKey.contains("createReservation")) {
		return new NotFoundException("Reservation service book or customer not found");
	    }
	}

	if (response.status() == 404) {
	    if (methodKey.contains("cancelReservation")) {
		return new NotFoundInDataBaseException("Reservation service reservation not found");
	    }
	}
	throw new UnknowErrorException();
    }

}
