package com.example.app.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Builder()
@Data
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @NonNull
    @Column
    public String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", nullable = false)
    public Set<GeologicalClass> geologicalClasses;
}
