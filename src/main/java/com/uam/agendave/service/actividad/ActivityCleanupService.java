package com.uam.agendave.service.actividad;

import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Registro;
import com.uam.agendave.repository.ActividadRepository;
import com.uam.agendave.repository.RegistroRepository;
import com.uam.agendave.service.notificacion.NotificationService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ActivityCleanupService implements LimpiarActividadService {

    private final ActividadRepository activityRepository;
    private final RegistroRepository registroRepository;
    private final NotificationService notificationService;

    public ActivityCleanupService(
            ActividadRepository activityRepository,
            RegistroRepository registroRepository,
            NotificationService notificationService
    ) {
        this.activityRepository = activityRepository;
        this.registroRepository = registroRepository;
        this.notificationService = notificationService;
    }

    /**
     * Runs daily at 00:00:00 — deactivates past activities.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    @Transactional
    public void deactivatePastActivities() {
        LocalDate today = LocalDate.now();
        List<Actividad> pastActive = activityRepository.findByFechaBeforeAndEstadoTrue(today);
        if (!pastActive.isEmpty()) {
            pastActive.forEach(a -> a.setEstado(false));
            activityRepository.saveAll(pastActive);
        }
    }

    /**
     * Runs every hour — checks for reminder times.
     */
    @Scheduled(cron = "0 0 * * * *") // every hour
    @Transactional
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Actividad> todas = activityRepository.findByEstado(true);

        for (Actividad actividad : todas) {
            LocalDate fecha = actividad.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime horaInicio = actividad.getHoraInicio().toLocalTime();
            LocalDateTime inicio = LocalDateTime.of(fecha, horaInicio);

            // 1 day before at 12:00 PM
            LocalDateTime recordatorio1 = LocalDateTime.of(fecha.minusDays(1), LocalTime.NOON);

            // same day, 1 hour before horaInicio
            LocalDateTime recordatorio2 = inicio.minusHours(1);

            if (now.isAfter(recordatorio1.minusMinutes(1)) && now.isBefore(recordatorio1.plusMinutes(1))) {
                enviarRecordatorio(actividad);
            }

            if (now.isAfter(recordatorio2.minusMinutes(1)) && now.isBefore(recordatorio2.plusMinutes(1))) {
                enviarRecordatorio(actividad);
            }
        }
    }

    private void enviarRecordatorio(Actividad actividad) {
        notificationService.notificarRecordatorio(actividad);
    }

    }
