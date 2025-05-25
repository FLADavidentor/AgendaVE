package com.uam.agendave.repository;

import com.uam.agendave.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<ImageData,Long> {
}
