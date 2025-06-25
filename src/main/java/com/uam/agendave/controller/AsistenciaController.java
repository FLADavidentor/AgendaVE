package com.uam.agendave.controller;

import com.uam.agendave.dto.Registro.AsistenciaDTO;
import com.uam.agendave.dto.Registro.InscritoBodyDTO;
import com.uam.agendave.dto.Registro.InscritoResponseDTO;
import com.uam.agendave.dto.Registro.RegistroDTO;
import com.uam.agendave.service.actividad.ActividadService;  // Asegúrate de importar esta clase
import com.uam.agendave.service.Registro.RegistroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asistencia")

public class AsistenciaController {

    private final RegistroService registroService;
    private final ActividadService actividadService;


    public AsistenciaController(RegistroService registroService, ActividadService actividadService) {
        this.registroService = registroService;
        this.actividadService = actividadService;  // Asignamos el servicio de Actividad
    }

    // Tiene que llegar un DTO que contenga el CIF del estudiante y el ID de la actividad
    // Como parametros me tienen que mandar un: Cif: String - Id de Actividad: String - Transporte:Boolean
    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @PostMapping("/confirmar_inscripcion")
    public ResponseEntity inscribirActividad(@RequestBody RegistroDTO registroDTO) {

        try {
            registroService.guardarRegistro(registroDTO, actividadService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(201).body(null);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @PostMapping("/{cif}/actividades_inscritas")
    public ResponseEntity<List<?>> actividadesInscritas(@PathVariable String cif) {
        try {
            return ResponseEntity.ok().body(registroService.buscarActividadesInscritasPorCif(cif));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @PostMapping("/marcar_asistencia")
    public ResponseEntity marcarAsistencia(@RequestBody AsistenciaDTO asistenciaDTO) {

        try{
            registroService.marcarAsistencia(asistenciaDTO);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.ok().body(null);
    }

    @PreAuthorize("hasRole('ESTUDIANTE')")
    @PostMapping("/verificar_Inscripcion")

    public ResponseEntity verificarInscripcion(@RequestBody InscritoBodyDTO inscritoBodyDTO) {
        try{
            registroService.verificarValorDeInscripcion(inscritoBodyDTO);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.ok().body(null);
    }



}

