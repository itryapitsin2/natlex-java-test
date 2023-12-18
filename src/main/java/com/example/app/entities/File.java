package com.example.app.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(chain = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Column
    public String data;

    @NonNull
    @Column
    @Enumerated(EnumType.ORDINAL)
    public Status status;

    @NonNull
    @Column
    @Enumerated
    public TaskType taskType;
}
