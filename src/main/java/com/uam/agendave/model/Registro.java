package com.uam.agendave.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
public class Registro extends Identifiable {

    private boolean convalidacion; // Indica si el registro incluye convalidación
    private boolean transporte;    // Indica si el estudiante usará transporte

    @ManyToOne
    @JoinColumn(name = "idEstudiante", nullable = false)
    private Estudiante estudiante; // Relación con el estudiante

    @ManyToOne
    @JoinColumn(name = "idActividad", nullable = false)
    private Actividad actividad;   // Relación con la actividad

    @ElementCollection
    @CollectionTable(name = "registro_convalidaciones", joinColumns = @JoinColumn(name = "idRegistro"))
    @MapKeyColumn(name = "tipoConvalidacion") // Enum como clave
    @Column(name = "cantidad")
    @Enumerated(EnumType.STRING) // Guardar el enum como String en la base de datos
    private Map<TipoConvalidacion, Integer> convalidacionesRealizadas; // Créditos convalidados por tipo

    @Column(name = "totalConvalidado")
    private int totalConvalidado; // Créditos totales convalidados por el estudiante en esta actividad

    /**
     * Valida que las convalidaciones realizadas no excedan los límites permitidos
     * definidos en la actividad para cada tipo y en total.
     */
    public void validarConvalidacion() {
        if (convalidacion) {
            // Obtener los límites directamente desde la actividad
            Map<TipoConvalidacion, Integer> limitesConvalidacion = actividad.getConvalidacionesPermitidas();
            Integer limiteTotal = actividad.getTotalConvalidacionesPermitidas();

            // Validar si la actividad permite convalidaciones
            if (limitesConvalidacion == null || limitesConvalidacion.isEmpty() || limiteTotal == null || limiteTotal <= 0) {
                throw new IllegalStateException("La actividad no permite convalidaciones.");
            }

            // Validar límites por tipo de convalidación
            convalidacionesRealizadas.forEach((tipo, cantidad) -> {
                int limiteTipo = limitesConvalidacion.getOrDefault(tipo, 0);
                if (cantidad > limiteTipo) {
                    throw new IllegalStateException("Excedido el límite para el tipo de convalidación: " + tipo);
                }
            });

            // Validar límite total
            if (totalConvalidado > limiteTotal) {
                throw new IllegalStateException("Excedido el límite total de convalidaciones permitidas para esta actividad.");
            }
        }
    }
}


