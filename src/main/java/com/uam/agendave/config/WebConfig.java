package com.uam.agendave.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // change "/backendFiles/**" to whatever URL path you wanna expose it under
        registry.addResourceHandler("/backendFiles/images/**")
                .addResourceLocations("file:/home/baxava/Pictures/backendImages");
    }
}