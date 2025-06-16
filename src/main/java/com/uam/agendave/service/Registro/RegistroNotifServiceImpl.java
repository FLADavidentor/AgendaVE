package com.uam.agendave.service.Registro;

import com.uam.agendave.dto.Registro.RegistroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegistroNotifServiceImpl implements RegistroNotifService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public RegistroNotifServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notificarRegistroCreado(RegistroDTO registroDTO) {
        messagingTemplate.convertAndSend("/topic/registro/creado", registroDTO);
    }

    @Override
    public void notificarRegistroEliminado(RegistroDTO registroDTO) {
        messagingTemplate.convertAndSend("/topic/registro/eliminado", registroDTO);
    }

}
