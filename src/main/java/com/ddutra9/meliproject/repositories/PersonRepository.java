package com.ddutra9.meliproject.repositories;

import com.ddutra9.meliproject.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT p FROM Person p where p.dna = :dna")
    List<Person> findFirstByDna(@Param("dna") String[] dna);

}
