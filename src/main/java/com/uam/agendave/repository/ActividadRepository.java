package com.uam.agendave.repository;

import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.TipoConvalidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ActividadRepository extends JpaRepository<Actividad, UUID> {
    // Métodos personalizados pueden agregarse aquí
    @Query("""
    SELECT a
    FROM Actividad a
    WHERE a.estado = true
      AND a.cupo > (SELECT COALESCE(COUNT(r), 0) FROM Registro r WHERE r.actividad = a)
""")
    List<Actividad> findActividadesConCupoDisponible();
    List<Actividad> findByNombreActividadNombre(String nombre);       // Filtrar por nombre de actividad
    List<Actividad> findByLugarId(UUID idLugar);      // Filtrar actividades por lugar
    @Query("""
SELECT key(c), value(c)
FROM Actividad a JOIN a.convalidacionesPermitidas c
WHERE a.id = :id
""")
    List<Object[]> findConvalidacionesById(UUID id);
}
