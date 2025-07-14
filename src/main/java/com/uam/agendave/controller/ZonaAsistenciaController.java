package com.uam.agendave.controller;

import com.uam.agendave.dto.EntidadesConfig.ZonaAsistenciaDTO;
import com.uam.agendave.service.entities.zona.AsistenciaConfigStoreService;
import com.uam.agendave.service.entities.zona.ZonaAsistenciaNotifService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/asistencia/zona")
@RequiredArgsConstructor
public class ZonaAsistenciaController {

    private final AsistenciaConfigStoreService asistenciaConfigStoreService;
    private final ZonaAsistenciaNotifService zonaAsistenciaNotifService;

    @PostMapping("/configurar")
    public void configurarZona(@Valid @RequestBody ZonaAsistenciaDTO zona) {
        // 1. Guardar configuración en memoria
        asistenciaConfigStoreService.guardarConfiguracion(
                zona.getIdActividad(),
                zona.getLat(),
                zona.getLng(),
                zona.getRadioMetros(),
                zona.getTiempoLimite()
        );

        // 2. Notificar por WebSocket
        zonaAsistenciaNotifService.notificarNuevaZona(zona);
    }

    @GetMapping("/{idActividad}")
    public ZonaAsistenciaDTO obtenerZona(@PathVariable UUID idActividad) {
        if (!asistenciaConfigStoreService.existeConfiguracion(idActividad)) {
            throw new IllegalArgumentException("No existe zona configurada para la actividad.");
        }

        var config = asistenciaConfigStoreService.obtenerConfiguracion(idActividad);
        return new ZonaAsistenciaDTO(
                idActividad,
                config.getRadioMetros(),
                config.getTiempoLimite()
        );
    }
}
