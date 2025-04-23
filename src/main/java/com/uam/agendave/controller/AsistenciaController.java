package com.uam.agendave.controller;

import com.uam.agendave.dto.AsistenciaDTO;
import com.uam.agendave.dto.RegistroDTO;
import com.uam.agendave.model.Actividad;
import com.uam.agendave.model.Registro;
import com.uam.agendave.model.TipoConvalidacion;
import com.uam.agendave.service.ActividadService;  // Asegúrate de importar esta clase
import com.uam.agendave.service.RegistroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/asistencia")
@CrossOrigin(origins = "*")
public class AsistenciaController {

    private final RegistroService registroService;
    private final ActividadService actividadService;


    public AsistenciaController(RegistroService registroService, ActividadService actividadService) {
        this.registroService = registroService;
        this.actividadService = actividadService;  // Asignamos el servicio de Actividad
    }

    // Tiene que llegar un DTO que contenga el CIF del estudiante y el ID de la actividad
    // Como parametros me tienen que mandar un: Cif: String - Id de Actividad: String - Transporte:Boolean
    @PostMapping("/confirmar_inscripcion")
    public ResponseEntity inscribirActividad(@RequestBody RegistroDTO registroDTO) {

        try {
            registroService.guardarRegistro(registroDTO, actividadService);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(201).body(null);
    }

    @PostMapping("/{cif}/actividades_inscritas")
    public ResponseEntity<List<?>> actividadesInscritas(@PathVariable String cif) {
        try {
            return ResponseEntity.ok().body(registroService.buscarActividadesInscritasPorCif(cif));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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



}

