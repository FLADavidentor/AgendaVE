package com.uam.agendave.controller;

import com.uam.agendave.dto.Notificaciones.CrearAnuncioRequest;
import com.uam.agendave.model.Notificacion;
import com.uam.agendave.model.TipoDestinatario;
import com.uam.agendave.repository.NotificationRepository;
import com.uam.agendave.service.notificacion.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificationRepository notificacionRepository;
    private NotificationService notificationService;

    @PreAuthorize("hasRole('ESTUDIANTE')")
    @GetMapping("/{cif}")
    public List<Notificacion> obtenerNotificaciones(@PathVariable String cif) {
        return notificacionRepository.findByDestinatarioIdOrTipoDestinatario(cif, TipoDestinatario.GLOBAL);
    }


    @PostMapping("/anuncio")
    @PreAuthorize("hasRole('ADMIN')")
    public void crearAnuncio(@RequestBody CrearAnuncioRequest request) {
        notificationService.notificarAnuncioGlobal(request.getAutor(), request.getMensaje());
    }

}

