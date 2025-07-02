package com.uam.agendave.manager;

import com.uam.agendave.dto.EntidadesConfig.ZonaAsistenciaDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ZonaAsistenciaManager {

    private final Map<UUID, ZonaAsistenciaDTO> zonasActivas = new ConcurrentHashMap<>();

    public void registrarZona(ZonaAsistenciaDTO zona) {
        zonasActivas.put(zona.getIdActividad(), zona);
    }

    public Optional<ZonaAsistenciaDTO> obtenerZona(UUID idActividad) {
        return Optional.ofNullable(zonasActivas.get(idActividad));
    }

    public void eliminarZona(UUID idActividad) {
        zonasActivas.remove(idActividad);
    }

    public void limpiarZonasExpiradas() {
        zonasActivas.entrySet().removeIf(entry ->
                entry.getValue().getTiempoLimite().isBefore(LocalDateTime.now()));
    }
}

