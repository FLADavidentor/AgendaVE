package com.uam.agendave.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class SimpleController {

    // Ruta principal
    @GetMapping
    public String mainPage() {
        return "Bienvenido a la API principal de AgendaVE";
    }

    @GetMapping("/saludo")
    public String getGreetings() {
        return "Hola mundo";
    }

    @PostMapping("/adios")
    public String getGoodBye() {
        return "Adios";
    }
}