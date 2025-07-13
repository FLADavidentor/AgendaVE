package com.uam.agendave.repository;

import com.uam.agendave.model.Notificacion;
import com.uam.agendave.model.TipoDestinatario;
import com.uam.agendave.model.TipoNotif;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notificacion, UUID> {

    void deleteByTipoAndTimestampBefore(TipoNotif tipo, LocalDateTime limit);

    List<Notificacion> findByDestinatarioIdOrTipoDestinatario(String destinatarioId, TipoDestinatario tipoDestinatario);

}
