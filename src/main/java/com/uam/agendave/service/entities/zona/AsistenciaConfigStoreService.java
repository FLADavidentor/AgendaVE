package com.uam.agendave.service.entities.zona;

import com.uam.agendave.entities.ConfigAsistenciaTemporal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AsistenciaConfigStoreService {

    private final Map<UUID, ConfigAsistenciaTemporal> configuraciones = new ConcurrentHashMap<>();

    public void guardarConfiguracion(UUID idActividad, double radioMetros, LocalDateTime tiempoLimite) {
        configuraciones.put(idActividad, new ConfigAsistenciaTemporal(radioMetros, tiempoLimite));
    }

    public ConfigAsistenciaTemporal obtenerConfiguracion(UUID idActividad) {
        return configuraciones.get(idActividad);
    }

    public boolean existeConfiguracion(UUID idActividad) {
        return configuraciones.containsKey(idActividad);
    }

    public void eliminarConfiguracion(UUID idActividad) {
        configuraciones.remove(idActividad);
    }

    public Map<UUID, ConfigAsistenciaTemporal> getTodasLasConfiguraciones() {
        return configuraciones;
    }
}
