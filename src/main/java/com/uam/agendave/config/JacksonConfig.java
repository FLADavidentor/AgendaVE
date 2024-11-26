package com.uam.agendave.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.util.TipoConvalidacionKeyDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(TipoConvalidacion.class, new TipoConvalidacionKeyDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}

