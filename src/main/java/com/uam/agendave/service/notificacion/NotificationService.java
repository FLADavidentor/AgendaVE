package com.uam.agendave.service.notificacion;

import com.uam.agendave.model.*;
import com.uam.agendave.repository.NotificationRepository;
import com.uam.agendave.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private NotificationRepository notificacionRepository;

    @Autowired
    private WebSocketNotificationService websocketService; // optional

    public void notificarCambioActividad(Actividad actividad, String campo, String antes, String despues) {
        List<Registro> registros = registroRepository.findByActividadId(actividad.getId());

        for (Registro r : registros) {
            Notificacion notif = new Notificacion();
            notif.setTipo(TipoNotif.ACTIVIDAD_ACTUALIZADA);
            notif.setTipoDestinatario(TipoDestinatario.CIF);
            notif.setDestinatarioId(r.getEstudiante().getCif());

            Map<String, String> datos = new HashMap<>();
            datos.put("actividadNombre", actividad.getNombre());
            datos.put("campo", campo);
            datos.put("antes", antes);
            datos.put("despues", despues);
            notif.setDatos(datos);

            notificacionRepository.save(notif);
            websocketService.enviarNotificacion(notif); // optional
        }

    }
    public void notificarActividadCancelada(Actividad actividad) {
        List<Registro> registros = registroRepository.findByActividadId(actividad.getId());

        for (Registro r : registros) {
            Notificacion notif = new Notificacion();
            notif.setTipo(TipoNotif.ACTIVIDAD_CANCELADA);
            notif.setTipoDestinatario(TipoDestinatario.CIF);
            notif.setDestinatarioId(r.getEstudiante().getCif());

            Map<String, String> datos = new HashMap<>();
            datos.put("actividadNombre", actividad.getNombre());
            notif.setDatos(datos);

            notificacionRepository.save(notif);
            websocketService.enviarNotificacion(notif);
        }
    }
    public void notificarRecordatorio(Actividad actividad) {
        List<Registro> registros = registroRepository.findByActividadId(actividad.getId());

        for (Registro r : registros) {
            Notificacion notif = new Notificacion();
            notif.setTipo(TipoNotif.ACTIVIDAD_RECORDATORIO);
            notif.setTipoDestinatario(TipoDestinatario.CIF);
            notif.setDestinatarioId(r.getEstudiante().getCif());

            Map<String, String> datos = new HashMap<>();
            datos.put("actividadNombre", actividad.getNombre());
            datos.put("fecha", actividad.getFecha().toString()); // or format nicely if needed
            notif.setDatos(datos);

            notificacionRepository.save(notif);
            websocketService.enviarNotificacion(notif);
        }
    }


    public void notificarAnuncioGlobal(String autor, String mensajeContenido) {
        Notificacion notif = new Notificacion();
        notif.setTipo(TipoNotif.ANUNCIO);
        notif.setTipoDestinatario(TipoDestinatario.GLOBAL); // 🧠 no destinatarioId, it's global

        Map<String, String> datos = new HashMap<>();
        datos.put("autor", autor);
        datos.put("mensaje", mensajeContenido);
        notif.setDatos(datos);

        notificacionRepository.save(notif);
        websocketService.enviarAnuncioGlobal(notif);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void eliminarAnunciosVencidos() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(2);
        notificacionRepository.deleteByTipoAndTimestampBefore(TipoNotif.ANUNCIO, cutoff);
    }









}
