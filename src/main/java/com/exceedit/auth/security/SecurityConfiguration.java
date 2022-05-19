package com.exceedit.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .and()
                .addFilterBefore(
        customAuthFilter(), BasicAuthenticationFilter.class)
                .formLogin()
                .loginPage("/oauth/authorize")
                .failureUrl("/login?error=true")
                .and()
                .authorizeRequests()
//                .antMatchers("/").permitAll().anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionFixation().none()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
//------------------------------------------------------------------------------------------------------
//                .authorizeRequests()
//                .antMatchers("/anonymous*").anonymous()
//                .antMatchers("/oauth*").permitAll()
////                .anyRequest().authenticated()
//                .anyRequest().permitAll()

//            .and()
//            .formLogin()
//                .loginPage("/oauth/authorize").permitAll()
//                .loginProcessingUrl("/oauth/login")
//                .successHandler(myAuthenticationSuccessHandler())
//                .failureUrl("/login.html?error=true")
//
//            .and()
//                .logout().deleteCookies("JSESSIONID")
//
//            .and()
//                .rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400)
//
//            .and()
//                .csrf().disable()
//            ;
    }
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

    @Bean
    public CustomHeaderAuthFilter customAuthFilter(){
        return new CustomHeaderAuthFilter();
    }
}
