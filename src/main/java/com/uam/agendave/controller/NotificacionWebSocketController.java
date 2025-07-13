package com.uam.agendave.controller;

import com.uam.agendave.model.Notificacion;
import com.uam.agendave.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class NotificacionWebSocketController {

    @Autowired
    private NotificationRepository notificacionRepository;

    @MessageMapping("/notificacion/leida")
    public void marcarComoLeidas(Map<String, List<String>> payload) {
        List<String> ids = payload.get("ids");
        if (ids == null) return;

        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();

        List<Notificacion> notifs = notificacionRepository.findAllById(uuids);
        for (Notificacion notif : notifs) {
            notif.setLeida(true);
        }

        notificacionRepository.saveAll(notifs);
    }

}