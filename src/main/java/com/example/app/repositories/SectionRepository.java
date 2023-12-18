package com.example.app.repositories;

import com.example.app.entities.Section;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long>, JpaSpecificationExecutor<Section> {

    @Query("SELECT section FROM Section section JOIN FETCH section.geologicalClasses geologicalClasses")
    List<Section> retrieveAll();

    @Query("SELECT section FROM Section section JOIN FETCH section.geologicalClasses geologicalClasses WHERE geologicalClasses.code = :code")
    List<Section> retrieveAllByCode(@Param("code") String code);
}
