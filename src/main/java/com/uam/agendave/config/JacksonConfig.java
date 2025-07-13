package com.uam.agendave.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.util.TipoConvalidacionKeyDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // register your custom stuff
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(TipoConvalidacion.class, new TipoConvalidacionKeyDeserializer());
        mapper.registerModule(module);

        // register Java 8 time module so it stops crying
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO 8601 is cleaner

        return mapper;
    }
}
