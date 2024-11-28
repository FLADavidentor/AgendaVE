package com.uam.agendave.util;

import com.uam.agendave.model.Estudiante;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ApiResponse {
    private boolean success;
    private String message;
    private List<Map<String, Object>> data; // Datos como lista de mapas
}

