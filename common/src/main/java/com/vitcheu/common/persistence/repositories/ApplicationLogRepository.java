package com.vitcheu.common.persistence.repositories;

import com.vitcheu.common.persistence.model.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationLogRepository
  extends JpaRepository<ApplicationLog, Long> {}
