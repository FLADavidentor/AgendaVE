package com.uam.agendave.model;

import com.uam.agendave.util.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Getter
@Setter
public class Notificacion extends Identifiable {

    @Enumerated(EnumType.STRING)
    private TipoNotif tipo;

    @Enumerated(EnumType.STRING)
    private TipoDestinatario tipoDestinatario;

    private String destinatarioId;

    @Convert(converter = MapToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, String> datos;

    private boolean leida = false;

    private java.time.LocalDateTime timestamp = java.time.LocalDateTime.now();
}

