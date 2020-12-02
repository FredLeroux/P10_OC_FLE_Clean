package std.libraryAPIGatewayZuul.errorDecoder;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;
import std.libraryAPIGatewayZuul.errorDecoder.exceptions.AuthTokenNotFoundException;
import std.libraryAPIGatewayZuul.errorDecoder.exceptions.CustomerNotFoundException;

@Component
public class libraryApiGatewayZuulErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		 if(response.status()==404) {
			 if(methodKey.contains("getCustomer")) {
				 return new CustomerNotFoundException("from zuul decoder");
			 }
		 }
		 if(response.status()==403) {
			 if(methodKey.contains("authToken")) {
				return new AuthTokenNotFoundException();
			 }
		 }

		return null;
	}

}
