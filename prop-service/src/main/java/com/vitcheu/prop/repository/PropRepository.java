package com.vitcheu.prop.repository;

import com.vitcheu.prop.model.Prop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PropRepository extends JpaRepository<Prop, Integer> {
  @Transactional
  @Modifying
  @Query(
    "UPDATE Prop p set p.quantity = p.quantity - :amount " +
    " WHERE p.id= :propId "
  )
  int updateAmount(Integer propId, int amount);
}
