package std.libraryEureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LibraryEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryEurekaApplication.class, args);
	}

}
