package com.uam.agendave.repository;

import com.uam.agendave.model.PromoImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PromoImagesRepository extends JpaRepository<PromoImages, UUID> {

    List<PromoImages> findAll();
    PromoImages findByImagenPath(String imagenPath);
}
