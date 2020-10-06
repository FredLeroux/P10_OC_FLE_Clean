package std.libraryUi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "std.libraryUi")
public class LibraryUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryUiApplication.class, args);
	}

}
