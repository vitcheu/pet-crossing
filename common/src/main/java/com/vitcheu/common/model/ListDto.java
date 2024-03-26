package com.vitcheu.common.model;

import java.util.List;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public abstract class ListDto<E> {


  @Getter
  private List<E> elements;
}
