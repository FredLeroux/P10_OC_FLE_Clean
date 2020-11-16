package std.libraryUi.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;




@Configuration
@EnableWebSecurity
public class LibraryAppSecurityConfig extends WebSecurityConfigurerAdapter {




//Allow use of security dependency w/o login auto implemented
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();//Allow postMapping on ui
		http.httpBasic().disable();
		http.headers().disable();;

	}








}
