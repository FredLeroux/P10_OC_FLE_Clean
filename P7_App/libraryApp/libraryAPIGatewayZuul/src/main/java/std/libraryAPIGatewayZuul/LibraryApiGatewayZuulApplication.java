package std.libraryAPIGatewayZuul;

import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
//TODO note (for presentation of later)  that to solve power issue and lag and all
///issue concerning nav i have (load balencer not server available pc freeze ...) supress feign on zuul now ther
//is only one feign this seem to solve the power , ram issue
public class LibraryApiGatewayZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiGatewayZuulApplication.class, args);
	}

}
