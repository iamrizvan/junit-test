package org.studyeasy.security;

import javax.servlet.http.HttpFilter;
import javax.ws.rs.HttpMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.studyeasy.service.UserService;


@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
		.disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
		.permitAll()
		.antMatchers(SecurityConstants.H2_CONSOLE)
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.addFilter(getAuthenticationFilter())   // required user credentials for login and generating token.
		.addFilter(new AuthorizationFilter(authenticationManager()))  // Required login generated token
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);   // to avoid session or cookie creation.
//		.addFilter(new AuthenticationFilter(authenticationManager()));
		
		http.headers().frameOptions().disable();
		
	}
	
	// Below code snippet is for allowUrlEncodedSlashHttpFirewall
	/*
	 * public HttpFirewall allowUrlEncodedSlashHttpFirewall() { StrictHttpFirewall
	 * firewall = new StrictHttpFirewall(); firewall.setAllowUrlEncodedSlash(true);
	 * return firewall; }
	 * 
	 * 
	 * public void
	 * configure(org.springframework.security.config.annotation.web.builders.
	 * WebSecurity web) throws Exception { super.configure(web);
	 * web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
	 * 
	 * }
	 */

	@Override protected void configure(AuthenticationManagerBuilder auth) throws
	  Exception { auth.userDetailsService(userService)
	  .passwordEncoder(bCryptPasswordEncoder);
	  }
	
	
	public AuthenticationFilter getAuthenticationFilter() throws Exception
	{
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");  // Customise authentication URL.
		return filter;
	}
	 	
}
