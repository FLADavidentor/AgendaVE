package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Registro extends Identifiable {

    private boolean convalidacion;
    private boolean transporte;

    @ManyToOne
    @JoinColumn(name = "idEstudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "idActividad", nullable = false)
    private Actividad actividad;

    @Enumerated(EnumType.STRING)
    private TipoConvalidacion tipoConvalidacion;

    public void validarConvalidacion() {
        if (convalidacion) {
            Map<TipoConvalidacion, Integer> convalidaciones = actividad.getConvalidacionesPermitidas();
            Integer totalPermitido = actividad.getTotalConvalidacionesPermitidas();

            if (convalidaciones == null || convalidaciones.isEmpty() || totalPermitido == null || totalPermitido <= 0) {
                throw new IllegalStateException("La actividad no permite convalidaciones.");
            }

            int usados = actividad.getRegistros() == null ? 0 : actividad.getRegistros().stream()
                    .filter(Registro::isConvalidacion)
                    .mapToInt(r -> convalidaciones.getOrDefault(r.getTipoConvalidacion(), 0))
                    .sum();

            if (usados >= totalPermitido) {
                throw new IllegalStateException("Se ha alcanzado el límite total de convalidaciones para esta actividad.");
            }
        }
    }
}


