package com.uam.agendave.dto.Registro;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InscritoBodyDTO {
    private String cif;
    private List<String> actividades;
}
