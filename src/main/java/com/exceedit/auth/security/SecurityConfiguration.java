package com.exceedit.auth.security;

import com.exceedit.auth.repository.security.JwtTokenRepository;
import com.exceedit.auth.security.filters.JwtCsrfFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    @Bean("authenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
//                .addFilterAt(new JwtCsrfFilter(jwtTokenRepository, resolver), CsrfFilter.class).csrf().ignoringAntMatchers("/**")
//                .and()
                .authorizeRequests()
                .antMatchers("/private")
                .fullyAuthenticated().and()
                .authorizeRequests()
                .antMatchers("/")
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/api/oauth")
                .and()
                .logout()
                .logoutUrl("/api/oauth/logout")
                .logoutSuccessUrl("/api/oauth/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }
}
