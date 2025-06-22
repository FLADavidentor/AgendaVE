package com.uam.agendave.service.Transporte;


import com.uam.agendave.dto.Actividad.TransporteDTO;
import com.uam.agendave.model.Transporte;

import java.util.List;
import java.util.UUID;

public interface TransporteService {
    List<TransporteDTO> obtenerTodos();
    TransporteDTO guardar(TransporteDTO transporteDTO);
    TransporteDTO buscarPorId(UUID id);
    void eliminar(UUID id);
    List<TransporteDTO> buscarPorLugar(String nombreLugar);
    Transporte guardarSiNoExiste(TransporteDTO transporteDTO);
    Transporte buscarEntidadPorId(UUID id);

}
