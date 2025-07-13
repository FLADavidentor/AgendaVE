package com.uam.agendave.service.notificacion;


import com.uam.agendave.dto.Notificaciones.NotificationResponse;
import com.uam.agendave.mapper.NotificationMapper;
import com.uam.agendave.model.Notificacion;
import com.uam.agendave.model.TipoDestinatario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationMapper notificationMapper;

    @Autowired
    public WebSocketNotificationService(SimpMessagingTemplate messagingTemplate,
                                         NotificationMapper notificationMapper) {
        this.messagingTemplate = messagingTemplate;
        this.notificationMapper = notificationMapper;
    }

    public void enviarNotificacion(Notificacion notif) {
        if (notif.getDestinatarioId() == null) return;

        String destino = "/topic/usuario/" + notif.getDestinatarioId();

        // Map entity to DTO with full details
        NotificationResponse payload = notificationMapper.toResponse(notif);

        // Send full DTO over WebSocket
        messagingTemplate.convertAndSend(destino, payload);
    }

    public void enviarAnuncioGlobal(Notificacion notif) {
        if (notif.getTipoDestinatario() != TipoDestinatario.GLOBAL) return;

        NotificationResponse payload = notificationMapper.toResponse(notif);

        messagingTemplate.convertAndSend("/topic/anuncios", payload);
    }


}