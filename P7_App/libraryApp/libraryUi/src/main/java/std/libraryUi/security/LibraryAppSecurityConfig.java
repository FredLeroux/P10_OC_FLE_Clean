package std.libraryUi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

		// http.csrf().disable();
		 http.authorizeRequests().antMatchers("/", "/login", "/logout","/letsGo").permitAll();

		http
		.authorizeRequests()
			.antMatchers("/booksList").hasAnyAuthority("TYPE_admin","TYPE_user")
			.and()
			.headers().frameOptions().sameOrigin()
			.and()
			.formLogin().loginPage("/login").loginProcessingUrl("/letsGo")
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/");
	}


	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(user).passwordEncoder(encoder.bCryptEncoder());
    }





}
