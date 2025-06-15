package com.uam.agendave.service.nombreActividad;

import com.uam.agendave.model.NombreActividad;

import java.util.List;


public interface NombreActividadService {
    List<NombreActividad> buscarPorNombre(String nombre);
    List<NombreActividad> buscarPorNombreParcial(String parteDelNombre);
    NombreActividad guardar(NombreActividad nombreActividad);

}
