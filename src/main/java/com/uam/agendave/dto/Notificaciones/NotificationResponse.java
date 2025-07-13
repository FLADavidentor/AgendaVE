package com.uam.agendave.dto.Notificaciones;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class NotificationResponse {
    private UUID id;
    private String tipo;
    private String mensaje;
    private Map<String, String> datos;
    private LocalDateTime timestamp;
    private boolean leida;
}
