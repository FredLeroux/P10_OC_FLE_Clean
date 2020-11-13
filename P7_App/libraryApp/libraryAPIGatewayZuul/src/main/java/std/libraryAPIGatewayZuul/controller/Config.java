package std.libraryAPIGatewayZuul.controller;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.post.LocationRewriteFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@EnableZuulProxy
public class Config {



	    @Bean
	    public LocationRewriteFilter locationRewriteFilter() {
	        return new LocationRewriteFilter();
	    }
	}


