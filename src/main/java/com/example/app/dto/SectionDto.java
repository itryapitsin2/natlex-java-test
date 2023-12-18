package com.example.app.dto;

import com.example.app.entities.GeologicalClass;
import com.example.app.entities.Section;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder()
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto {

    private Long id;

    private String name;

    private Set<GeologicalClassDto> geologicalClasses = new HashSet<>();

    public static SectionDto fromSection(Section section) {
        return SectionDto
                .builder()
                .name(section.getName())
                .id(section.getId())
                .geologicalClasses(
                        section
                                .getGeologicalClasses()
                                .stream()
                                .map(
                                        geologicalClass ->
                                                GeologicalClassDto
                                                        .builder()
                                                        .id(geologicalClass.getId())
                                                        .name(geologicalClass.getName())
                                                        .code(geologicalClass.getCode())
                                                        .build()
                                )
                                .collect(Collectors.toSet())
                )
                .build();
    }

    public Section toSection() {
        var geologicalClasses = this.getGeologicalClasses().stream().map(
                geologicalClass ->
                        GeologicalClass
                                .builder()
                                .id(this.getId())
                                .name(geologicalClass.getName())
                                .code(geologicalClass.getCode())
                                .build()
        ).collect(Collectors.toSet());
        return Section
                .builder()
                .name(this.getName())
                .id(this.getId())
                .geologicalClasses(geologicalClasses)
                .build();
    }
}
