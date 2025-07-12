package com.uam.agendave.service.imagen;

import com.uam.agendave.dto.Imagen.PromoImagesRequest;
import com.uam.agendave.dto.Imagen.PromoImagesResponse;
import com.uam.agendave.mapper.ImagenPromoMapper;
import com.uam.agendave.model.PromoImages;
import com.uam.agendave.repository.PromoImagesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImagenPromoService {
    private ImageStorageService imageStorageService;
    private PromoImagesRepository promoImagesRepository;
    private ImagenPromoMapper imagenPromoMapper;
    public ImagenPromoService(ImageStorageService imageStorageService,PromoImagesRepository promoImagesRepository, ImagenPromoMapper imagenPromoMapper) {
        this.imageStorageService = imageStorageService;
        this.promoImagesRepository = promoImagesRepository;
        this.imagenPromoMapper = imagenPromoMapper;
    }

    public ResponseEntity <?> CrearImagenPromo(PromoImagesRequest promoImagesRequest){
        try{
            PromoImages  promoImages = new PromoImages();

            promoImages.setImagenPath(imageStorageService.saveImage(promoImagesRequest.getImagenBase64()));
            promoImagesRepository.save(promoImages);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Imagen Creada Correctamente"
            ));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "Error inesperado al Crear imagen Promo",
                    "error", e.getMessage()
            ));
        }
    }

    public ResponseEntity<?> EliminarImagenPromo(List<PromoImagesResponse> promoImagesResponses) {
        try {
            List<PromoImages> promoImages = new ArrayList<>();

            for (PromoImagesResponse response : promoImagesResponses) {
                String path = response.getImagenPath();

                PromoImages image = promoImagesRepository.findByImagenPath(path);
                if (image != null) {
                    promoImages.add(image);


                    imageStorageService.deleteImageIfUnused(path, 1); // force delete
                }
            }

            promoImagesRepository.deleteAll(promoImages);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "imágenes eliminadas exitosamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "error inesperado al eliminar imagen",
                    "error", e.getMessage()
            ));
        }
    }

    public ResponseEntity<?> ObtenerImagenesPromo() {
        try {
            List<PromoImages> entities = promoImagesRepository.findAll();
            List<PromoImagesResponse> promoImages = imagenPromoMapper.toDtoList(entities);

            return ResponseEntity.ok(promoImages);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "error al obtener las imágenes promo",
                    "error", e.getMessage()
            ));
        }
    }

}
