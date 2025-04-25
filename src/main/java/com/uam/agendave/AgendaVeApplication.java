package com.uam.agendave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AgendaVeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendaVeApplication.class, args);
    }

}
