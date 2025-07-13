package com.uam.agendave.mapper;

import com.uam.agendave.dto.Notificaciones.NotificationResponse;
import com.uam.agendave.model.Notificacion;
import com.uam.agendave.service.notificacion.NotificacionMensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    @Autowired
    private NotificacionMensajeService mensajeService;

    public NotificationResponse toResponse(Notificacion notif) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(notif.getId());
        dto.setTipo(notif.getTipo().name());
        dto.setMensaje(mensajeService.generarMensaje(notif));
        dto.setDatos(notif.getDatos());
        dto.setTimestamp(notif.getTimestamp());
        dto.setLeida(notif.isLeida());
        return dto;
    }
}
