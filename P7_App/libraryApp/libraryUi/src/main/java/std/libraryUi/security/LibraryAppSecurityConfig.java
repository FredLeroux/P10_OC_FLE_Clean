package std.libraryUi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




//@Configuration
//@EnableWebSecurity
public class LibraryAppSecurityConfig extends WebSecurityConfigurerAdapter {




//Allow use of security dependency w/o login auto implemented
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.httpBasic().disable();
		http.headers().frameOptions().sameOrigin();

	}








}
