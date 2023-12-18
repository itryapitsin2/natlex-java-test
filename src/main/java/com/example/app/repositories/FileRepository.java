package com.example.app.repositories;

import com.example.app.entities.File;
import com.example.app.entities.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    @Query("SELECT file.status FROM File file WHERE file.id = :id")
    Status findStatusById(@Param("id") Long id);
}
