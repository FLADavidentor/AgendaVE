package com.uam.agendave.service;

import com.uam.agendave.model.Registro;

import java.util.List;
import java.util.UUID;

public interface RegistroService {
    List<Registro> obtenerTodos();
    Registro guardarRegistro(Registro registro);
    Registro buscarPorId(UUID id);
    void eliminarRegistro(UUID id);
    List<Registro> buscarPorEstudiante(UUID idEstudiante);
    List<Registro> buscarPorActividad(UUID idActividad);
}
