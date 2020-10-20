package std.libraryUi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.Authentication;

import feign.auth.BasicAuthRequestInterceptor;

//@Configuration
public class FeignSecurity {

	@Bean
	public BasicAuthRequestInterceptor basicRe() {
		return new BasicAuthRequestInterceptor("user","123");
	}

}
