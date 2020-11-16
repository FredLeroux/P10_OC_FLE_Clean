package std.libraryAPIGatewayZuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableFeignClients(basePackages = "std.libraryAPIGatewayZuul")
public class LibraryApiGatewayZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiGatewayZuulApplication.class, args);
	}

}
