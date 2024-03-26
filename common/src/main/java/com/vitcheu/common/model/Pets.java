package com.vitcheu.common.model;

import java.util.List;

public class Pets extends ListDto<PetDetails> {
  public Pets(){
    super();
  }

  public Pets(List<PetDetails> elements) {
    super(elements);
  }
}
