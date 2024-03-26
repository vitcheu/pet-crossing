package com.vitcheu.pet.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.core.style.ToStringCreator;

@Data
@Entity
@Table(name = "pets")
@NoArgsConstructor
public class Pet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "birth_date")
  @Temporal(TemporalType.DATE)
  private Date birthDate;

  @ManyToOne
  @JoinColumn(name = "type_id")
  @JsonSerialize(using = PetTypeSerializer.class)
  private PetType type;

  @Column(name = "owner_id")
  private long ownerId;

  @OneToOne(
    optional = false,
    cascade = CascadeType.ALL
  )
  @JoinColumn(name = "properties_id")
  private PetPropertiesPo properties;

  @Override
  public String toString() {
    return new ToStringCreator(this)
      .append("id", this.getId())
      .append("name", this.getName())
      .append("birthDate", this.getBirthDate())
      .append("type", this.getType().getName())
      .append("Owner Id", this.ownerId)
      .toString();
  }
}
