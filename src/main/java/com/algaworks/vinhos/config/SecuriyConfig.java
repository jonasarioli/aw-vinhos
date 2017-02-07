package com.algaworks.vinhos.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

@EnableWebSecurity
public class SecuriyConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("jonas").password("jonas").roles("PESQUISAR_VINHO").and()
		.withUser("admin").password("admin").roles("CADASTRAR_VINHO", "PESQUISAR_VINHO");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/layout/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/vinhos").hasRole("PESQUISAR_VINHO")
		.antMatchers("/vinhos/**").hasRole("CADASTRAR_VINHO")
		.anyRequest().authenticated()
		.and()
		.formLogin().loginPage("/login").permitAll().and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
}
