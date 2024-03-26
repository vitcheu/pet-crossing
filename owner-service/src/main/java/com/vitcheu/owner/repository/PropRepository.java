package com.vitcheu.owner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.vitcheu.owner.model.po.OwnedProp;


@Repository
public interface PropRepository extends JpaRepository<OwnedProp, Long> {
  Optional<OwnedProp> findByPropId(int propId);

  Optional<OwnedProp> findByPropIdAndOwnerId(int propId, Long ownerId);

  @Modifying
  @Query(
    "update OwnedProp op set amount=amount+ :updateValue where op.propId= :propId and op.ownerId= :ownerId"
  )
  void updateCount(int propId, long ownerId, int updateValue);

  @Modifying
  void deleteByOwnerIdAndPropId(long ownerId, int propId);
}
