package com.vitcheu.owner.repository;

import com.vitcheu.owner.model.po.PetPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetPo, Integer> {
  void deleteByPetIdAndOwnerId(Integer petId, Long ownerId);
}
