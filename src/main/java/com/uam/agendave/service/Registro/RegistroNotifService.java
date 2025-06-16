package com.uam.agendave.service.Registro;

import com.uam.agendave.dto.Registro.RegistroDTO;

public interface RegistroNotifService {
    void notificarRegistroCreado(RegistroDTO registroDTO);
    void notificarRegistroEliminado(RegistroDTO registroDTO);
}
