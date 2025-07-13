package com.uam.agendave.service.notificacion;

import com.uam.agendave.model.Notificacion;
import com.uam.agendave.model.TipoNotif;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificacionMensajeService {

    private static final Map<TipoNotif, String> templates = Map.of(
            TipoNotif.ACTIVIDAD_ACTUALIZADA,
            "La actividad '{actividadNombre}' fue actualizada: el campo '{campo}' cambió de '{antes}' a '{despues}'.",
            TipoNotif.ACTIVIDAD_CANCELADA,
            "La actividad '{actividadNombre}' ha sido cancelada.",
            TipoNotif.ACTIVIDAD_RECORDATORIO,
            "Recordatorio: la actividad '{actividadNombre}' será el {fecha}.",
            TipoNotif.REGISTRO_ASISTENCIA,
            "Se registró tu asistencia en '{actividadNombre}'.",
            TipoNotif.CREDITO_OBTENIDO,
            "Has obtenido {cantidad} crédito(s) en '{actividadNombre}'.",
            TipoNotif.ANUNCIO,
            "Anuncio de {autor}: \"{mensaje}\""
    );

    public String generarMensaje(Notificacion notif) {
        String template = templates.getOrDefault(
                notif.getTipo(),
                "Tienes una nueva notificación."
        );

        return render(template, notif.getDatos());
    }

    private String render(String template, Map<String, String> datos) {
        if (datos == null) return template;

        for (Map.Entry<String, String> entry : datos.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }

        return template;
    }
}
