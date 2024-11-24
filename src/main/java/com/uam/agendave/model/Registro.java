package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Entity
@Getter
@Setter
public class Registro extends Identifiable {

    private boolean convalidacion;
    private boolean transporte;

    @ManyToOne
    @JoinColumn(name = "idEstudiante")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "idActividad")
    private Actividad actividad;

    @Enumerated(EnumType.STRING) // Guardar el enum como String en la base de datos
    private TipoConvalidacion tipoConvalidacion;

    public void validarConvalidacion() {
        if (convalidacion) {
            Map<TipoConvalidacion, Integer> convalidaciones = actividad.getConvalidacionesPermitidas();
            Integer totalPermitido = actividad.getTotalConvalidacionesPermitidas();

            // Verifica si la actividad permite convalidaciones
            if (convalidaciones == null || convalidaciones.isEmpty() || totalPermitido == null || totalPermitido <= 0) {
                throw new IllegalStateException("La actividad no permite convalidaciones.");
            }

            // Calcula el total de convalidaciones usadas
            int usados = actividad.getRegistros().stream()
                    .filter(Registro::isConvalidacion)
                    .mapToInt(r -> convalidaciones.getOrDefault(r.getTipoConvalidacion(), 0))
                    .sum();

            if (usados >= totalPermitido) {
                throw new IllegalStateException("Se ha alcanzado el límite total de convalidaciones para esta actividad.");
            }
        }
    }
}

