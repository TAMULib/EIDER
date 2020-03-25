package edu.tamu.eider.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import edu.tamu.eider.app.model.Entity;
import edu.tamu.eider.app.model.Identifier;
import edu.tamu.eider.app.model.IdentifierType;
import edu.tamu.eider.app.model.Name;

@Configuration
public class RepositoryConfig {

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> {
            config.exposeIdsFor(Entity.class, Identifier.class, IdentifierType.class, Name.class);
        });
    }

}