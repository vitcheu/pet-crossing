package com.vitcheu.common.model;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PetProperties implements java.io.Serializable{
  private static final long serialVersionUID = 1L;

  int atk;

  int def;

  int hp;

  int mp;

  int speed;

  int luck;

  int favorability;

  private void changProperty(
    PetPropertiesName propertiesName,
    int value,
    BiFunction<Integer, Integer, Integer> valueCalculator
  ) {
    changFiledByReflect(
      this.getClass(),
      propertiesName.declaredName(),
      old -> valueCalculator.apply(old, value)
    );
  }

  private void changFiledByReflect(
    java.lang.Class<? extends com.vitcheu.common.model.PetProperties> pc,
    String propertiesName,
    Function<Integer, Integer> valueTransFormer
  ) {
    try {
      Field field = pc.getDeclaredField(propertiesName);
      field.setAccessible(true);

      Integer oldValue = (Integer) field.get(this);
      val newValue = valueTransFormer.apply(oldValue);
      log.debug(
        "PetProperties.changFiledByReflect():(%s -> %s)".formatted(
            oldValue,
            newValue
          )
      );

      field.set(this, newValue);
    } catch (
      NoSuchFieldException
      | SecurityException
      | IllegalArgumentException
      | IllegalAccessException e
    ) {
      throw new RuntimeException(e);
    }
  }

  /**
   * chang the value of the given field
   * by changvalue
   */
  public void changPropertyByValue(
    PetPropertiesName propertiesName,
    int changValue
  ) {
    log.debug(String.format("chang %s by %s ", propertiesName, changValue));
    changProperty(propertiesName, changValue, (Integer t, Integer u) -> t + u);
  }

  /**
   * chang the value of the given field
   * by time
   */
  public void changPropertyByTime(PetPropertiesName propertiesName, int time) {
    log.debug(String.format("chang %s by %s times", propertiesName, time));
    changProperty(propertiesName, time, (Integer t, Integer u) -> t * u);
  }

  public void changPropertyToValue(
    PetPropertiesName propertiesName,
    int desValue
  ) {
    log.debug(String.format("chang %s to %s", propertiesName, desValue));
    changProperty(propertiesName, desValue, (Integer t, Integer u) -> u);
  }
}
