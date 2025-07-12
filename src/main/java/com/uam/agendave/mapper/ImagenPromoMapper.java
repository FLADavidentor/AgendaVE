package com.uam.agendave.mapper;

import com.uam.agendave.dto.Imagen.PromoImagesResponse;
import com.uam.agendave.model.PromoImages;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImagenPromoMapper {

    public PromoImagesResponse toDto(PromoImages entity) {
        PromoImagesResponse dto = new PromoImagesResponse();
        dto.setImagenPath(entity.getImagenPath());
        return dto;
    }

    public List<PromoImagesResponse> toDtoList(List<PromoImages> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
