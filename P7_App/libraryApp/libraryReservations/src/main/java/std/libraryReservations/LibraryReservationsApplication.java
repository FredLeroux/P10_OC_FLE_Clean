package std.libraryReservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LibraryReservationsApplication {

    public static void main(String[] args) {
	SpringApplication.run(LibraryReservationsApplication.class, args);
    }

}
