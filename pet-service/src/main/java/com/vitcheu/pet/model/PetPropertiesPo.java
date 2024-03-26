package com.vitcheu.pet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * The additional  properties of a pet,
 * eg. The ATK, DEF, HP, MP, SPEED, LUCK,
 * and Favorability
 */
@Entity
@Table(name = "pet_properties")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PetPropertiesPo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  Integer id;

  int atk;

  int def;

  int hp;

  int mp;

  int speed;

  int luck;

  int favorability;

  public static PetPropertiesPo defualtProperties() {
    return new PetPropertiesPo(25, 5, 50, 30, 8, 10, 10);
  }

  public PetPropertiesPo(
    int atk,
    int def,
    int hp,
    int mp,
    int speed,
    int luck,
    int favorability
  ) {
    this.atk = atk;
    this.def = def;
    this.hp = hp;
    this.mp = mp;
    this.speed = speed;
    this.luck = luck;
    this.favorability = favorability;
  }
}
