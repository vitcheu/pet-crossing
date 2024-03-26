package com.vitcheu.owner.model.po;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "owned_prop")
@NoArgsConstructor
public class OwnedProp implements Serializable{
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  @Column(name = "prop_id")
  int propId;

  @Column(name = "amount")
  int amount;

  @Column(name = "owner_id")
  Long ownerId;

  public OwnedProp(int propId, int amount, Long ownerId) {
    this.propId = propId;
    this.amount = amount;
    this.ownerId = ownerId;
  }
}
