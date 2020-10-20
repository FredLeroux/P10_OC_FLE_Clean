package std.libraryUi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import std.libraryUi.security.configClass.UnauthentificatedRequest;
import std.libraryUi.security.encoder.LibraryEncoder;
import std.libraryUi.security.service.LibraryUserDetailsService;

@Configuration
@EnableWebSecurity
public class LibraryAppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LibraryUserDetailsService user;

	@Autowired
	private LibraryEncoder encoder;

	@Bean
	public UnauthentificatedRequest UnauthentificatedRequest() {
		return new UnauthentificatedRequest();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/log")
			.permitAll()
			.antMatchers("/booksList")
			.hasAnyAuthority("TYPE_admin","TYPE_user")
			.and()
			.formLogin()
			.loginPage("/login").permitAll()
			.and().logout().permitAll()
			.and()
			.headers()
			.frameOptions()
			.sameOrigin()
			.and().exceptionHandling().authenticationEntryPoint(UnauthentificatedRequest())
			;
	}


	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(user).passwordEncoder(encoder.bCryptEncoder());
    }





}
