package com.vitcheu.common.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vitcheu.common.persistence.model.BuiltWith;

@Repository
public interface BuiltWithRepository
  extends JpaRepository<BuiltWith, Integer> {}
