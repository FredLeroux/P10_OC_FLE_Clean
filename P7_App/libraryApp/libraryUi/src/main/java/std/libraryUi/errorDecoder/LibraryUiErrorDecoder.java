package std.libraryUi.errorDecoder;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;
import std.libraryUi.errorDecoder.exceptions.ChronoUnitNotImplementedException;
import std.libraryUi.errorDecoder.exceptions.LoanNotFoundException;
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
		System.out.println("unknown");
		throw new UnknowErrorException();
	}

}
