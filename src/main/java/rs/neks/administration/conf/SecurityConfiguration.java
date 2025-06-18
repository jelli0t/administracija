package rs.neks.administration.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "rs.neks.administration.security")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService operatorService;
	
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//        	.passwordEncoder(passwordEncoder())
//        	.withUser("office@neks.rs").password(passwordEncoder().encode("n3k50ffic3")).roles("USER");
		 auth.authenticationProvider(authenticationProvider());
    }
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
	    	.anyRequest().authenticated()
	    	.and().formLogin()
	    	.loginPage("/login").permitAll()
	    	.defaultSuccessUrl("/")
	    	.and()
            .logout()                                    
                .permitAll();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	
	 @Bean
	 public DaoAuthenticationProvider authenticationProvider() {
		 DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		 authenticationProvider.setPasswordEncoder(passwordEncoder());
		 authenticationProvider.setUserDetailsService(operatorService);
//			ReflectionSaltSource saltHash = new ReflectionSaltSource();
//			saltHash.setUserPropertyToUse("username");
//			daoProvider.setSaltSource(saltHash);
	     return authenticationProvider;
	 }
	 
}