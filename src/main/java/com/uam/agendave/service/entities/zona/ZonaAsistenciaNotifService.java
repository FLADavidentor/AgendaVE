package com.uam.agendave.service.entities.zona;

import com.uam.agendave.dto.EntidadesConfig.ZonaAsistenciaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZonaAsistenciaNotifService {

    private final SimpMessagingTemplate messagingTemplate;

    public void notificarNuevaZona(ZonaAsistenciaDTO zona) {
        String destino = "/topic/asistencia/zona/" + zona.getIdActividad();
        messagingTemplate.convertAndSend(destino, zona);
    }
}