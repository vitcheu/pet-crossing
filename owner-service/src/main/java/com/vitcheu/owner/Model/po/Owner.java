package com.vitcheu.owner.model.po;

import com.vitcheu.common.exception.ResourceNotFoundException;
import com.vitcheu.common.exception.UnsupportedOperationException;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "owner")
public class Owner implements Serializable{
  private static final long serialVersionUID = 1L;

  @Id
  long userId;

  int money;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "owner_id")
  List<OwnedProp> ownedProps;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "owner_id")
  List<PetPo> pets;

  public void addPet(PetPo petPo) {
    Assert.isTrue(pets != null && !pets.contains(petPo));
    pets.add(petPo);
  }

  public void removePet(int petId) {
    pets.removeIf(pet -> pet.getPetId() == petId);
  }

  public boolean hasProp(int propId) {
    return ownedProps.stream().anyMatch(prop -> prop.getPropId() == propId);
  }

  /**
   * Add a prop to the owner.
   * @param propId
   * @param amount
   * @return the new prop if it's a new prop, null otherwise.
   */
  public OwnedProp addProp(int propId, int amount) {
    Assert.isTrue(ownedProps != null);

    for (OwnedProp prop : ownedProps) {
      if (prop.getPropId() == propId) {
        var current = prop.getAmount();
        prop.setAmount(current + amount);
        return null;
      }
    }

    var newProp = new OwnedProp(propId, amount, userId);
    ownedProps.add(newProp);

    return newProp;
  }

  public void updatePropsAmount(int propId, int amount) {
    Assert.isTrue(ownedProps != null);

    for (OwnedProp prop : ownedProps) {
      if (prop.getPropId() == propId) {
        var current = prop.getAmount();
        if (current + amount < 0) {
          throw new UnsupportedOperationException(
            "No enough props#" + propId + " to consume."
          );
        } else if (current + amount == 0) {
          ownedProps.remove(prop);
        }

        prop.setAmount(current + amount);
        return;
      }
    }

    throw new ResourceNotFoundException(
      "Prop#" + propId + " of " + userId + " not found."
    );
  }
}
