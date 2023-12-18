package com.example.app.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class GeologicalClassDto {

    public Long id;

    @NonNull
    public String name;

    @NonNull
    public String code;
}
