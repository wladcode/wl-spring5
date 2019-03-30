package com.bolsa.factura.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String ROL_ADMIN="ADMIN";
	private static final String ROL_USER="USER";

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//rutas publicas
		http.authorizeRequests()
		.antMatchers("/", "/css/**","/js/**","/images/**", "/listar").permitAll()
		.antMatchers("/ver/**").hasAnyRole(ROL_USER)
		.antMatchers("/uploads/**").hasAnyRole(ROL_USER)
		
		.antMatchers("/form/**").hasAnyRole(ROL_ADMIN)
		.antMatchers("/editar/**").hasAnyRole(ROL_ADMIN)
		.antMatchers("/delete/**").hasAnyRole(ROL_ADMIN)
		
		
		.antMatchers("/factura/**").hasAnyRole(ROL_ADMIN)
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403")
		;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserBuilder userBuilder = User.builder().passwordEncoder(encoder::encode);

		build.inMemoryAuthentication()
		.withUser(userBuilder.username("admin").password("admin").roles(ROL_ADMIN, ROL_USER))
		.withUser(userBuilder.username("andres").password("andres").roles(ROL_USER));
	}

	
	
	
	
	

}
