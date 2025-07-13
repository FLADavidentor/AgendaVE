package com.uam.agendave.dto.Notificaciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearAnuncioRequest {
    private String autor;
    private String mensaje;
}