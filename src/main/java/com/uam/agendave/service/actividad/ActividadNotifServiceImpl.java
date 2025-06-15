package com.uam.agendave.service.actividad;

import com.uam.agendave.dto.ActividadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActividadNotifServiceImpl implements ActividadNotifService {
private final SimpMessagingTemplate messagingTemplate;

@Autowired
public ActividadNotifServiceImpl(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
}

@Override
    public void notificarActividadPublicada(ActividadDTO actividadDTO) {
    messagingTemplate.convertAndSend("/topic/actividad/publicada", actividadDTO);
}

@Override
    public void notificarActividadDesPublicada(ActividadDTO actividadDTO) {
        messagingTemplate.convertAndSend("/topic/actividad/publicada", actividadDTO);
}

}
