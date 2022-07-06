package com.exceedit.auth.web;

import com.exceedit.auth.web.interceptors.RequestsLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;


@EnableWebMvc
@Configuration
@ComponentScan("com.exceedit.auth.web.controller")
public class MvcConfig implements WebMvcConfigurer {

    //TODO fix it doesn't work
    @Value("#{'${cors.app.allowed.origins}'.split(',')}")
    private List<String> ALLOWED_ORIGINS;

    public MvcConfig() {
        super();
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestsLogger());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")
                .allowedOrigins("http://localhost:3000")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}