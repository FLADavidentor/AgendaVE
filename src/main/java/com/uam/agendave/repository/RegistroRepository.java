package com.uam.agendave.repository;

import com.uam.agendave.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface RegistroRepository extends JpaRepository<Registro, UUID> {

    List<Registro> findByCif(String cif);

    @Query("SELECT r FROM Registro r WHERE r.actividad.id = :idActividad")
    List<Registro> findByActividadId(@Param("idActividad") UUID idActividad);

    long countByActividadId(UUID idActividad);
}