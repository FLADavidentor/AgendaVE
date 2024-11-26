package com.uam.agendave.service;

import com.uam.agendave.model.NombreActividad;

import java.util.List;
import java.util.UUID;

public interface NombreActividadService {
    List<NombreActividad> buscarPorNombre(String nombre);
    List<NombreActividad> buscarPorNombreParcial(String parteDelNombre);
    List<NombreActividad> buscarPorTipoActividad(UUID idTipoActividad);
    NombreActividad guardar(NombreActividad nombreActividad);

}