package com.uam.agendave.repository;

import com.uam.agendave.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RegistroRepository extends JpaRepository<Registro, UUID> {

    List<Registro> findByEstudianteId(UUID idEstudiante);

    @Query("SELECT r FROM Registro r WHERE r.actividad.id = :idActividad")
    List<Registro> findByActividadId(@Param("idActividad") UUID idActividad);
}