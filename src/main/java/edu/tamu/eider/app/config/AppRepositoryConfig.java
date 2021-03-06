package edu.tamu.eider.app.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.IdentifierType;
import edu.tamu.eider.app.model.Name;
import edu.tamu.eider.app.model.handler.IdentifierEventHandler;
import edu.tamu.eider.app.web.converter.PlainTextHttpMessageConverter;

@Configuration
public class AppRepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Entity.class, Identifier.class, IdentifierType.class, Name.class);
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
    }

    @Override
    public void configureHttpMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(0, new PlainTextHttpMessageConverter());
        RepositoryRestConfigurer.super.configureHttpMessageConverters(messageConverters);
    }

    @Bean
    public IdentifierEventHandler identifierEventHandler() {
        return new IdentifierEventHandler();
    }

}
