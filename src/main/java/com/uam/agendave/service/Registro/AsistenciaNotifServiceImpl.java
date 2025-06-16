package com.uam.agendave.service.Registro;

import com.uam.agendave.dto.Registro.AsistenciaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaNotifServiceImpl implements AsistenciaNotifService {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public AsistenciaNotifServiceImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.messagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void notifAsistenciaEstado(AsistenciaDTO asistenciaDTO) {
        messagingTemplate.convertAndSend("/topic/Asistencia/Actualizado", asistenciaDTO);
    }
}
