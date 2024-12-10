package com.uam.agendave.repository;

import com.uam.agendave.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String name);
}
