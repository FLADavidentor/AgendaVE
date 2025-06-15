package com.uam.agendave.service.actividad;

import com.uam.agendave.model.Actividad;
import com.uam.agendave.repository.ActividadRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityCleanupService implements LimpiarActividadService {

    private final ActividadRepository activityRepository;

    public ActivityCleanupService(ActividadRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Se ejecuta todos los días a las 00:00:00
     * Cron: segundo minuto hora díaDelMes mes díaDeLaSemana
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
}
