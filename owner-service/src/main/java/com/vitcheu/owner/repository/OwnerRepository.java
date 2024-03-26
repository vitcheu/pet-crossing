package com.vitcheu.owner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.vitcheu.owner.model.po.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
  @Override
  @NonNull List<Owner> findAll();

  @Modifying
  @Query(
    "update Owner ow set ow.money = ow.money+ :updateValue where ow.userId= :userId "
  )
  void updateMoney(long userId, int updateValue);
}
