package uk.co.olimor.BMBTApi_boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * The user from the application.properties file.
	 */
	@Value("${login.user}")
	private String user;
	
	/**
	 * The password from the application.properties file.
	 */
	@Value("${login.password}")
	private String password;
	
	/**
	 * {@link SecurityUtil} instance.
	 */
	@Autowired
	private SecurityUtil securityUtil;
	
	/**
	 * Configure the security layer.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.anyRequest().authenticated()
				.and()
				// We filter the api/login requests
				.addFilterBefore(new LoginFilter("/login", authenticationManager()), 
						UsernamePasswordAuthenticationFilter.class)
				// And filter other requests to check the presence of JWT in header
				.addFilterBefore(new AuthenticationFilter(securityUtil), UsernamePasswordAuthenticationFilter.class);
	}

	/**
	 * Setup default authentication.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Create a default account
		auth.inMemoryAuthentication()
			.withUser(user).password(password).roles("ADMIN");
	}
}