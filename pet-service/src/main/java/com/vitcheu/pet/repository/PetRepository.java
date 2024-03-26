package com.vitcheu.pet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vitcheu.pet.model.Pet;
import com.vitcheu.pet.model.PetType;

public interface PetRepository extends JpaRepository<Pet, Integer> {
  @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
  List<PetType> findPetTypes();

  @Query("FROM PetType ptype WHERE ptype.id = :typeId")
  Optional<PetType> findPetTypeById(@Param("typeId") int typeId);

}
