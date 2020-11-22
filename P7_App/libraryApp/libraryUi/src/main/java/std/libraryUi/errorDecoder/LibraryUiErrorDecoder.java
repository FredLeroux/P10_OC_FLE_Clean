package std.libraryUi.errorDecoder;

import feign.Response;
import feign.codec.ErrorDecoder;;

public class LibraryUiErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		// TODO Auto-generated method stub

		// not to discrimnat use if method key contains name of the method i.e if loan
		// service return an error on getloan method use .conatis getLoan to
		// discriminate
		if (response.status() == 404) {
			if (methodKey.contains("postponeLoan")) {
				// TODO exception = LoanNotFoundException;
			}
			if (methodKey.contains("returnLoan")) {
				// TODO exception = LoanUnknownException
			}
		}

		if (response.status() == 501) {
			if (methodKey.contains("postponeLoan")) {
				// TODO exception = ChronoUnitNotImplementedException
			}
		}

		return null;
	}

}
