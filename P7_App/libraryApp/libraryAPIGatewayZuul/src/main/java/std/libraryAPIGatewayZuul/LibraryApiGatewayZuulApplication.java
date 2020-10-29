package std.libraryAPIGatewayZuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class LibraryApiGatewayZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiGatewayZuulApplication.class, args);
	}

}
