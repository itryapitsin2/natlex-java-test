package com.example.app.entities;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@AllArgsConstructor
public class GeologicalClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NonNull
    @Column
    public String name;

    @NonNull
    @Column
    public String code;
}
