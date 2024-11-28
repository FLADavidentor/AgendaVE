package com.uam.agendave.util;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    // Elimina la clave secreta ya que no validaremos el token localmente

    // Método para simplemente devolver el token como está
    public String storeToken(String token) {
        // Este método no realiza ninguna validación
        return token;
    }

    // Puedes agregar otros métodos para manipular el token si es necesario
}



