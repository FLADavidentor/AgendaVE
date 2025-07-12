package com.uam.agendave.controller;

import com.uam.agendave.dto.Imagen.PromoImagesRequest;
import com.uam.agendave.dto.Imagen.PromoImagesResponse;
import com.uam.agendave.service.imagen.ImagenPromoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/promo")
public class PromoImagesController {
    private final ImagenPromoService imagenPromoService;

    public PromoImagesController(ImagenPromoService imagenPromoService) {
        this.imagenPromoService = imagenPromoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PromoImagesRequest promoImagesRequest) {

        return imagenPromoService.CrearImagenPromo(promoImagesRequest);

    }

    @PreAuthorize("hasAnyRole('ADMIN','ESTUDIANTE')")
    @GetMapping("/get_promo")
    public ResponseEntity<?> getPromo() {

        return imagenPromoService.ObtenerImagenesPromo();

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody List<PromoImagesResponse> promoImagesResponses) {

        return imagenPromoService.EliminarImagenPromo(promoImagesResponses);

    }
}
