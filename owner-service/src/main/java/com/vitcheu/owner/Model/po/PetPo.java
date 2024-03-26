package com.vitcheu.owner.model.po;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "t_pets")
@AllArgsConstructor
@NoArgsConstructor
public class PetPo implements Serializable{
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "pet_id")
  Integer petId;

  @Column(name = "owner_id")
  Long ownerId;
}
