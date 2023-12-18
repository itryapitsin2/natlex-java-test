package com.example.app.repositories;

import com.example.app.entities.GeologicalClass;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeologicalClassesRepository extends CrudRepository<GeologicalClass, Long> {
//
//    @Query("SELECT gc FROM geological_class gc WHERE section_id = :id")
//    List<GeologicalClass> findAllBySectionId(Long id);
//
//    @Query("SELECT gc FROM geological_class gc WHERE code = :code")
//    List<GeologicalClass> findAllByCode(String code);
}
