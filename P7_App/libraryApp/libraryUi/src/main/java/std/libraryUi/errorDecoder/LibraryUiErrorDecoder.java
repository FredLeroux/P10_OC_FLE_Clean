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
		// TODO Auto-generated method stub
		System.out.println("method key is :       ******************************"+ methodKey);
		// note to discriminate use if method key contains name of the method i.e if loan
		// service return an error on getloan method use .contains getLoan to
		// discriminate
		if (response.status() == 404) {
			if (methodKey.contains("postpone")) {
				System.out.println("postpone 4040");
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
