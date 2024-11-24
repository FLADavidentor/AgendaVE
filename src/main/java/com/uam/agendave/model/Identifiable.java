package com.uam.agendave.model;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public class Identifiable {
    @Id
    @UuidGenerator()
    private UUID id;
}
