package com.uam.agendave.dto.Imagen;

import com.uam.agendave.model.TipoImagen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenesParaListaDTO {
    private String imagenPath;
    private TipoImagen tipoImagen;
}
