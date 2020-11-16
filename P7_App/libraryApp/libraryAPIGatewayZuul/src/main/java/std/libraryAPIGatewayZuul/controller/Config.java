package std.libraryAPIGatewayZuul.controller;

import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableZuulProxy
public class Config {



	    @Bean
	    public LocationRewriteFilter locationRewriteFilter() {
	        return new LocationRewriteFilter();
	    }
	}


