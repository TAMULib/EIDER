package edu.tamu.eider.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class AppWebMvcConfig implements WebMvcConfigurer {

    @Value("${app.security.allow-access")
    private String[] hosts;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(hosts)
            .allowedMethods("GET", "DELETE", "PUT", "POST")
            .allowedHeaders("Origin", "Content-Type", "Access-Control-Allow-Origin", "x-requested-with", "data", "x-forwarded-for");
    }
}
