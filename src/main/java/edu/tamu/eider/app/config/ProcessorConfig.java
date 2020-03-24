package edu.tamu.eider.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.tamu.eider.app.model.processor.EntityProcessor;

@Configuration
public class ProcessorConfig {

    @Bean
    EntityProcessor entityProcessor() {
        return new EntityProcessor();
    }
}